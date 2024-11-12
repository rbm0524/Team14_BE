package com.ordertogether.team14_be.spot.mapper;

import com.ordertogether.team14_be.member.application.service.MemberService;
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
	SpotMapper INSTANCE = Mappers.getMapper(SpotMapper.class);

	@BeanMapping(ignoreByDefault = false)
	@Mapping(source = "member.id", target = "memberId")
	SpotDto toDto(Spot spot);

	@BeanMapping(ignoreByDefault = false)
	@Mapping(target = "category", ignore = true) // category는 무시
	SpotDto toSpotDto(SpotCreationRequest spotCreationRequest);

	@BeanMapping(ignoreByDefault = false)
	@Mapping(target = "member", ignore = true)
	Spot toEntity(SpotDto spotDto);

	@BeanMapping(
			nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
			ignoreByDefault = false)
	Spot toEntity(SpotDto spotDto, @MappingTarget Spot spot);

	@BeanMapping(ignoreByDefault = false)
	@Mapping(target = "member", expression = "java(memberService.findMember(spotDto.getMemberId()))")
	Spot toEntity(SpotDto spotDto, @Context MemberService memberService);

	@BeanMapping(ignoreByDefault = false)
	@Mapping(target = "category", expression = "java(spotDto.getCategory().getStringCategory())")
	SpotCreationResponse toSpotCreationResponse(SpotDto spotDto);

	@BeanMapping(ignoreByDefault = false)
	@Mapping(target = "category", expression = "java(spotDto.getCategory().getStringCategory())")
	SpotDetailResponse toSpotDetailResponse(SpotDto spotDto);

	@BeanMapping(ignoreByDefault = false)
	@Mapping(target = "category", expression = "java(spotDto.getCategory().getStringCategory())")
	SpotViewedResponse toSpotViewedResponse(SpotDto spotDto);

	@BeanMapping(ignoreByDefault = false)
	@Mapping(target = "category", expression = "java(spotDto.getCategory().getStringCategory())")
	SpotModifyResponse toSpotModifyResponse(SpotDto spotDto);

	@BeanMapping(ignoreByDefault = false)
	@Mapping(target = "category", ignore = true) // category는 무시
	SpotDto toSpotDto(SpotModifyRequest spotModifyRequest);

	// SpotCreationRequest에서 SpotDto로 매핑할 때 category 매핑
	@AfterMapping
	default void mapCategory(
			SpotCreationRequest spotCreationRequest, @MappingTarget SpotDto spotDto) {
		spotDto.setCategory(
				Category.fromStringToEnum(spotCreationRequest.category())
						.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다.")));
	}
}
