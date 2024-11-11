package com.ordertogether.team14_be.spot.service;

import ch.hsr.geohash.GeoHash;
import com.ordertogether.team14_be.spot.dto.controllerdto.SpotCreationResponse;
import com.ordertogether.team14_be.spot.dto.controllerdto.SpotDetailResponse;
import com.ordertogether.team14_be.spot.dto.controllerdto.SpotModifyResponse;
import com.ordertogether.team14_be.spot.dto.controllerdto.SpotViewedResponse;
import com.ordertogether.team14_be.spot.dto.servicedto.SpotDto;
import com.ordertogether.team14_be.spot.entity.Spot;
import com.ordertogether.team14_be.spot.exception.NotSpotMasterException;
import com.ordertogether.team14_be.spot.mapper.SpotMapper;
import com.ordertogether.team14_be.spot.repository.SpotRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpotService {
	private final SpotRepository spotRepository;

	// Spot 상세 조회하기
	@Transactional(readOnly = true)
	public List<SpotDetailResponse> getSpotDetail(Long id, Long memberId) {
		return spotRepository.findByMemberIdAndIsDeletedFalse(memberId).stream()
				.map(SpotMapper.INSTANCE::toSpotDetailResponse)
				.toList();
	}

	@Transactional
	public SpotCreationResponse createSpot(SpotDto spotDto, Long memberId) {
		spotDto.setMemberId(memberId);
		GeoHash geoHash =
				GeoHash.withCharacterPrecision(
						spotDto.getLat().doubleValue(), spotDto.getLng().doubleValue(), 12);
		spotDto.setGeoHash(geoHash.toBase32());
		spotDto.setCreatedAt(LocalDateTime.now());
		spotDto.setCreatedBy(spotDto.getMemberId());
		spotDto.setModifiedAt(LocalDateTime.now());
		spotDto.setModifiedBy(spotDto.getMemberId());
		Spot spot = SpotMapper.INSTANCE.toEntity(spotDto);
		log.info("Spot 생성 요청: {}", spot);
		return SpotMapper.INSTANCE.toSpotCreationResponse(spotRepository.save(spot));
	}

	@Transactional(readOnly = true)
	public List<SpotViewedResponse> getSpotByGeoHash(BigDecimal lat, BigDecimal lng) {
		int precision = 12;
		GeoHash geoHash =
				GeoHash.withCharacterPrecision(lat.doubleValue(), lng.doubleValue(), precision);

		String hashString = geoHash.toBase32();

		List<SpotDto> resultAroundSpot = spotRepository.findBygeoHash(hashString);
		return resultAroundSpot.stream().map(SpotMapper.INSTANCE::toSpotViewedResponse).toList();
	}

	@Transactional
	public SpotModifyResponse updateSpot(SpotDto spotDto, Long memberId) {
		if (!Objects.equals(memberId, spotDto.getCreatedBy())) {
			throw new NotSpotMasterException("작성자만 수정할 수 있습니다.");
		}
		spotDto.setModifiedAt(LocalDateTime.now());
		spotDto.setMemberId(memberId);
		SpotDto modifiedSpotDto = spotRepository.update(spotDto);
		log.info("Spot 수정 요청: {}", modifiedSpotDto);
		return SpotMapper.INSTANCE.toSpotModifyResponse(modifiedSpotDto);
	}

	@Transactional
	public void deleteSpot(Long id, Long memberId) {
		SpotDto spotDto = spotRepository.findByIdAndIsDeletedFalse(id);
		// id가 createdBy와 일치하는지 검증 후 delete
		if (!Objects.equals(spotDto.getCreatedBy(), memberId)) {
			throw new IllegalArgumentException("방장이 아닌 사람은 삭제할 수 없습니다.");
		}
		spotRepository.delete(id);
	}
}
