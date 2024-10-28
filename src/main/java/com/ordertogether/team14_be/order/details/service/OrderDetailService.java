package com.ordertogether.team14_be.order.details.service;

import com.ordertogether.team14_be.member.persistence.MemberRepository;
import com.ordertogether.team14_be.member.persistence.entity.Member;
import com.ordertogether.team14_be.order.details.dto.create.CreateOrderDetailRequestDto;
import com.ordertogether.team14_be.order.details.dto.create.CreateOrderDetailResponseDto;
import com.ordertogether.team14_be.order.details.dto.get.GetOrdersInfoRequestDto;
import com.ordertogether.team14_be.order.details.dto.get.GetOrdersInfoResponseDto;
import com.ordertogether.team14_be.order.details.dto.get.OrderInfo;
import com.ordertogether.team14_be.order.details.entity.OrderDetail;
import com.ordertogether.team14_be.order.details.repository.OrderDetailRepository;
import com.ordertogether.team14_be.spot.entity.Spot;
import com.ordertogether.team14_be.spot.repository.SimpleSpotRepository;
import com.ordertogether.team14_be.spot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderDetailService {
	private final SimpleSpotRepository simpleSpotRepository;
	private final OrderDetailRepository orderDetailRepository;
	private final MemberRepository memberRepository;
	private final SpotRepository spotRepository;

	// 주문 상세 정보 생성 메서드
	@Transactional
	public CreateOrderDetailResponseDto createOrderDetail(
			CreateOrderDetailRequestDto createOrderDetailRequestDto) {

		// 참여자 본인 정보 설정
		Member member =
				memberRepository
						.findById(createOrderDetailRequestDto.getParticipantId())
						.orElseThrow(() -> new IllegalArgumentException("참여자 정보가 없습니다."));

		// 스팟 정보 설정 - spotMapper가 아직 구현되지 않아서 Simple사용
		//		SpotDto spotDto =
		//				spotRepository.findByIdAndIsDeletedFalse(createOrderDetailRequestDto.getSpotId());
		Spot spot =
				simpleSpotRepository
						.findById(createOrderDetailRequestDto.getSpotId())
						.orElseThrow(() -> new IllegalArgumentException("스팟 정보가 없습니다."));

		OrderDetail orderDetail =
				OrderDetail.builder()
						.member(member)
						.spot(spot)
						.price(createOrderDetailRequestDto.getPrice())
						.isPayed(createOrderDetailRequestDto.isPayed())
						.build();

		OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);

		return CreateOrderDetailResponseDto.builder()
				.id(savedOrderDetail.getId())
				.price(savedOrderDetail.getPrice())
				.isPayed(savedOrderDetail.isPayed())
				.participantName(savedOrderDetail.getMember().getDeliveryName())
				.spotName(spot.getStoreName())
				.build();
	}

	@Transactional(readOnly = true)
	public GetOrdersInfoResponseDto getOrdersInfo(Member member, GetOrdersInfoRequestDto dto) {
		Page<OrderDetail> orderDetails =
				orderDetailRepository.findAllByMember(
						member,
						PageRequest.of(
								dto.page(),
								dto.size(),
								dto.sort() == null ? Sort.unsorted() : Sort.by(dto.sort().get(1))));

		return new GetOrdersInfoResponseDto(
				orderDetails.getTotalPages(),
				orderDetails.getTotalElements(),
				orderDetails.getContent().stream()
						.map(
								order -> {
									Spot spot = order.getSpot();
									return new OrderInfo(
											order.getId(),
											spot.getCategory().toString(),
											spot.getStoreName(),
											spot.getMinimumOrderAmount(),
											spot.getPickUpLocation(),
											spot.getDeliveryStatus(),
											order.getPrice(),
											spot.getMember().getId().equals(member.getId()));
								})
						.toList());
	}
}
