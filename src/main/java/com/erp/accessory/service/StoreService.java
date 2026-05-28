package com.erp.accessory.service;

import com.erp.accessory.dto.request.StaffRequest;
import com.erp.accessory.dto.request.StoreRequest;
import com.erp.accessory.entity.AuthUser;
import com.erp.accessory.entity.Staff;
import com.erp.accessory.entity.Store;
import com.erp.accessory.exception.CustomException;
import com.erp.accessory.exception.ErrorCode;
import com.erp.accessory.mapper.StoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 점포·직원 관리 서비스
 */
@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreMapper storeMapper;
    private final PasswordEncoder passwordEncoder;

    public List<Store> getAllStores() {
        return storeMapper.findAllStores();
    }

    public Store getStoreById(Integer storeId) {
        Store store = storeMapper.findStoreById(storeId);
        if (store == null) throw new CustomException(ErrorCode.NOT_FOUND, "점포 ID: " + storeId);
        return store;
    }

    @Transactional
    public Store createStore(StoreRequest req) {
        Store store = new Store();
        store.setName(req.getName());
        store.setAddress(req.getAddress());
        store.setPhone(req.getPhone());
        store.setOpenedAt(req.getOpenedAt());
        storeMapper.insertStore(store);
        return store;
    }

    @Transactional
    public void updateStore(Integer storeId, StoreRequest req) {
        Store store = getStoreById(storeId);
        store.setName(req.getName());
        store.setAddress(req.getAddress());
        store.setPhone(req.getPhone());
        storeMapper.updateStore(store);
    }

    public List<Staff> getStaff(Integer storeId) {
        return storeMapper.findAllStaff(storeId);
    }

    @Transactional
    public Staff createStaff(StaffRequest req) {
        Staff staff = new Staff();
        staff.setStoreId(req.getStoreId());
        staff.setName(req.getName());
        staff.setPhone(req.getPhone());
        staff.setRole(req.getRole());
        storeMapper.insertStaff(staff);

        AuthUser authUser = new AuthUser();
        authUser.setStaffId(staff.getStaffId());
        authUser.setUsername(req.getUsername());
        authUser.setHashedPassword(passwordEncoder.encode(req.getPassword()));
        storeMapper.insertAuthUser(authUser);
        return staff;
    }

    @Transactional
    public void changePassword(Integer staffId, String newPassword) {
        storeMapper.updatePassword(staffId, passwordEncoder.encode(newPassword));
    }
}
