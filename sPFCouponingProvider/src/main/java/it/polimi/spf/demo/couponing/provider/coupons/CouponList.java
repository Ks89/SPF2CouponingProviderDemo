/*
 * Copyright 2015 Stefano Cappa
 *
 * This file is part of SPF.
 *
 * SPF is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * SPF is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with SPF.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package it.polimi.spf.demo.couponing.provider.coupons;

import java.util.ArrayList;
import java.util.List;

import it.polimi.spf.demo.couponing.provider.Coupon;
import lombok.Getter;

/**
 * Created by Stefano Cappa on 22/06/15.
 */
public class CouponList {

    @Getter private final List<Coupon> couponList;


    private static final CouponList instance = new CouponList();

    /**
     * Method to get the instance of this class.
     *
     * @return instance of this class.
     */
    public static CouponList getInstance() {
        return instance;
    }

    /**
     * Private constructor, because is a singleton class.
     */
    private CouponList() {
        couponList = new ArrayList<>();
    }
}