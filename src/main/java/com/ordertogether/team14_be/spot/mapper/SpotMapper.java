package com.ordertogether.team14_be.spot.mapper;

import com.ordertogether.team14_be.member.application.service.MemberService;
import com.ordertogether.team14_be.member.persistence.entity.Member;
import com.ordertogether.team14_be.spot.dto.controllerdto.*;
import com.ordertogether.team14_be.spot.dto.servicedto.SpotDto;
import com.ordertogether.team14_be.spot.entity.Spot;
import com.ordertogether.team14_be.spot.enums.Category;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(
		componentModel = "spring",
		unmappedTargetPolicy = ReportingPolicy.IGNORE) // Spring Bean으로 등록
public interface SpotMapper {
	SpotMapper INSTANCE = Mappers.getMapper(SpotMapper.class); // 객체 생성해서 INSTANCE에 할당

	@BeanMapping(ignoreByDefault = false)
	@Mapping(source = "member.id", target = "memberId") // memberId를 member로 매핑
	@Mapping(target = "category", ignore = true) // category는 무시
	SpotDto toDto(Spot spot);

	@BeanMapping(ignoreByDefault = false)
	@Mapping(target = "category", ignore = true) // category는 무시
	SpotDto toSpotDto(SpotCreationRequest spotCreationRequest);

	@BeanMapping(ignoreByDefault = false)
	@Mapping(target = "member", ignore = true)
	Spot toEntity(SpotDto spotDto);

	@BeanMapping(ignoreByDefault = false)
	Spot toEntity(SpotDto spotDto, @MappingTarget Spot spot); // 생성 또는 수정할 때 사용

	@BeanMapping(ignoreByDefault = false) // 자동 매핑 활성화
	@Mapping(target = "category", expression = "java(spotDto.getCategory().getStringCategory())")
	SpotCreationResponse toSpotCreationResponse(SpotDto spotDto);

	@BeanMapping(ignoreByDefault = false) // 자동 매핑 활성화
	@Mapping(target = "category", expression = "java(spotDto.getCategory().getStringCategory())")
	SpotDetailResponse toSpotDetailResponse(SpotDto spotDto);

	@BeanMapping(ignoreByDefault = false) // 자동 매핑 활성화
	@Mapping(target = "category", expression = "java(spotDto.getCategory().getStringCategory())")
	SpotViewedResponse toSpotViewedResponse(SpotDto spotDto);

	@BeanMapping(ignoreByDefault = false) // 자동 매핑 활성화
	@Mapping(target = "category", expression = "java(spotDto.getCategory().getStringCategory())")
	SpotModifyResponse toSpotModifyResponse(SpotDto spotDto);

	@BeanMapping(ignoreByDefault = false)
	@Mapping(target = "category", ignore = true) // category는 무시
	SpotDto toSpotDto(SpotModifyRequest spotModifyRequest);

	@AfterMapping
	default SpotDto map(SpotCreationRequest spotCreationRequest, @MappingTarget SpotDto spotDto) {
		spotDto.setCategory(
				Category.fromStringToEnum(spotCreationRequest.category())
						.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다.")));

		return spotDto;
	}

	@AfterMapping
	default SpotDto map(Spot spot, @MappingTarget SpotDto spotDto) {
		spotDto.setCategory(
				Category.fromStringToEnum(spot.getCategory().getCode())
						.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다.")));
		return spotDto;
	}

	// Long (memberId) -> Member 객체로 변환
	@AfterMapping
	default Member map(Long memberId, MemberService memberService) {
		if (memberId == null) {
			return null;
		}
		return memberService.findMember(memberId);
	}
}
