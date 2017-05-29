package com.huai.user.dao;

import com.huai.common.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created by huai23 on 2017/5/29.
 */
@Repository
public interface StaffRepository {

    @Select("select * from td_staff where staff_id = #{staff_id} and password = #{passwprd}")
    User checkUserLogin(@Param("staff_id") String staff_id,@Param("passwprd") String passwprd);


}
