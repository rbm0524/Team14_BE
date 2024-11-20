package com.ordertogether.team14_be.spot.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ch.hsr.geohash.GeoHash;
import com.ordertogether.team14_be.member.application.service.MemberService;
import com.ordertogether.team14_be.member.persistence.entity.Member;
import com.ordertogether.team14_be.order.details.service.OrderDetailService;
import com.ordertogether.team14_be.spot.dto.controllerdto.SpotCreationResponse;
import com.ordertogether.team14_be.spot.dto.controllerdto.SpotModifyResponse;
import com.ordertogether.team14_be.spot.dto.controllerdto.SpotViewedResponse;
import com.ordertogether.team14_be.spot.dto.servicedto.SpotDto;
import com.ordertogether.team14_be.spot.entity.Spot;
import com.ordertogether.team14_be.spot.enums.Category;
import com.ordertogether.team14_be.spot.repository.SpotRepository;
import com.ordertogether.team14_be.spot.service.SpotService;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SpotServiceTest {

	@Mock private MemberService memberService;
	@Mock private SpotRepository spotRepository;
	@Mock private OrderDetailService orderDetailService;
	@InjectMocks private SpotService spotService;

	private BigDecimal lat;
	private BigDecimal lng;
	private SpotDto spotDto;

	@BeforeEach
	void setUp() {
		lat = new BigDecimal("37.7749");
		lng = new BigDecimal("-122.4194");

		// SpotDto 초기화
		spotDto =
				SpotDto.builder()
						.id(1L)
						.lat(lat)
						.lng(lng)
						.category(Category.BURGER)
						.storeName("맥도날드")
						.memberId(1L)
						.minimumOrderAmount(12000)
						.deadlineTime(LocalTime.of(12, 0, 0))
						.pickUpLocation("픽업위치")
						.createdBy(1L)
						.geoHash("9q8yyk8ytpxr")
						.build();
	}

	@Test
	void createSpot_success() {
		when(spotRepository.save(any(Spot.class))).thenReturn(spotDto);
		when(memberService.findMember(1L))
				.thenReturn(new Member(1L, "example@naver.com", 12000, "010-1234-1234", "닉네임", "배민"));

		SpotCreationResponse response = spotService.createSpot(spotDto, 1L);

		assertNotNull(response);
		assertEquals(spotDto.getId(), response.id());
		assertEquals(1L, spotDto.getMemberId());
		assertEquals(spotDto.getLat(), response.lat());
		assertEquals(spotDto.getLng(), response.lng());
		assertEquals(spotDto.getCategory().getStringCategory(), response.category());
		assertEquals(spotDto.getStoreName(), response.storeName());
		assertEquals(spotDto.getMinimumOrderAmount(), response.minimumOrderAmount());
		assertEquals(spotDto.getPickUpLocation(), response.pickUpLocation());

		verify(spotRepository).save(any(Spot.class));
		verify(orderDetailService).createOrderDetail(any());
	}

	@Test
	void getSpotByGeoHash_success() {
		GeoHash geoHash = GeoHash.withCharacterPrecision(lat.doubleValue(), lng.doubleValue(), 5);
		String geoHashString = geoHash.toBase32().substring(0, 5);

		when(spotRepository.findBygeoHash(geoHashString)).thenReturn(List.of(spotDto));

		List<SpotViewedResponse> result = spotService.getSpotByGeoHash(lat, lng);

		assertFalse(result.isEmpty());
		assertThat(result.get(0).id()).isEqualTo(1L);
		assertThat(result.get(0).lat()).isEqualTo(lat);
		assertThat(result.get(0).lng()).isEqualTo(lng);
		assertThat(result.get(0).category()).isEqualTo("패스트푸드");
		assertThat(result.get(0).storeName()).isEqualTo(spotDto.getStoreName());
		assertThat(result.get(0).minimumOrderAmount()).isEqualTo(spotDto.getMinimumOrderAmount());
		assertThat(result.get(0).pickUpLocation()).isEqualTo(spotDto.getPickUpLocation());
		assertThat(result.get(0).deadlineTime()).isEqualTo(spotDto.getDeadlineTime());

		verify(spotRepository).findBygeoHash(geoHashString);
	}

	@Test
	void updateSpot_success() {
		SpotDto existingSpotDto = SpotDto.builder().id(1L).memberId(1L).createdBy(1L).build();

		when(spotRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(existingSpotDto);
		when(spotRepository.update(any(Spot.class))).thenReturn(spotDto);
		when(memberService.findMember(1L))
				.thenReturn(
						new Member(
								1L, "example@domain.com", 100, "010-1234-5678", "Delivery Name", "Platform"));

		SpotModifyResponse response = spotService.updateSpot(spotDto, 1L);

		assertEquals(spotDto.getLat(), response.lat());
		assertEquals(spotDto.getLng(), response.lng());
		assertEquals(spotDto.getCategory().getStringCategory(), response.category());
		assertEquals(spotDto.getStoreName(), response.storeName());
		assertEquals(spotDto.getMinimumOrderAmount(), response.minimumOrderAmount());
		assertEquals(spotDto.getPickUpLocation(), response.pickUpLocation());
	}

	@Test
	void updateSpot_throwsNotSpotMasterException() {
		SpotDto spotDtoWithDifferentCreator =
				SpotDto.builder().id(1L).memberId(2L).createdBy(2L).build();
		when(spotRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(spotDtoWithDifferentCreator);

		assertThrows(IllegalArgumentException.class, () -> spotService.updateSpot(spotDto, 1L));
	}

	@Test
	void deleteSpot_success() {
		// given
		Long id = 1L;

		// when
		spotService.deleteSpot(1L);

		// then
		verify(spotRepository, times(1)).delete(id);
	}
}
