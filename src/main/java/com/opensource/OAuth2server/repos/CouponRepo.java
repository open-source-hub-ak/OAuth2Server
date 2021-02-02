package com.opensource.OAuth2server.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.opensource.OAuth2server.model.Coupon;

public interface CouponRepo extends JpaRepository<Coupon, Long> {

	Coupon findByCode(String code);
}
