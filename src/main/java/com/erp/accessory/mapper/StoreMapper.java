package com.erp.accessory.mapper;

import com.erp.accessory.entity.AuthUser;
import com.erp.accessory.entity.Staff;
import com.erp.accessory.entity.Store;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 점포·직원·인증 매퍼
 */
@Mapper
public interface StoreMapper {

    // 점포
    List<Store> findAllStores();
    Store findStoreById(@Param("storeId") Integer storeId);
    void insertStore(Store store);
    void updateStore(Store store);

    // 직원
    List<Staff> findAllStaff(@Param("storeId") Integer storeId);
    Staff findStaffById(@Param("staffId") Integer staffId);
    void insertStaff(Staff staff);
    void updateStaff(Staff staff);

    // 인증
    void insertAuthUser(AuthUser authUser);
    AuthUser findAuthUserByUsername(@Param("username") String username);
    AuthUser findAuthUserById(@Param("userId") Integer userId);
    void updateLastLogin(@Param("userId") Integer userId);
    void updatePassword(@Param("staffId") Integer staffId,
                        @Param("hashedPassword") String hashedPassword);
}
