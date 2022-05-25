package com.nhom4web.dao.impl;

import com.nhom4web.dao.IDanhMucDAO;
import com.nhom4web.model.DanhMuc;
import com.nhom4web.model.Sach;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class DanhMucDAO extends AbstractDAO<DanhMuc> implements IDanhMucDAO {
    public DanhMucDAO() {
        super("danhMuc");
    }

    @Override
    protected List<DanhMuc> sangThucThes(ResultSet rs) {
        List<DanhMuc> danhMucs = new ArrayList<>();
        try {
            while (rs.next()) danhMucs.add(this.rsSangThucThe(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhMucs;
    }

    @Override
    protected DanhMuc sangThucThe(ResultSet rs) {
        try {
            if (rs.next()) return this.rsSangThucThe(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected LinkedHashMap<String, Object> sangMap(DanhMuc danhMuc) {
        LinkedHashMap<String, Object> duLieu = new LinkedHashMap<>();
        if (danhMuc.getMa() > 0) duLieu.put("ma", danhMuc.getMa());
        if (danhMuc.getTen() != null) duLieu.put("tenDanhMuc", danhMuc.getTen());
        if (danhMuc.getThoiGianTao() != null) duLieu.put("thoiGianTao", danhMuc.getThoiGianTao());
        if (danhMuc.getThoiGianCapNhat() != null) duLieu.put("thoiGianCapNhat", danhMuc.getThoiGianCapNhat());
        return duLieu;
    }

    @Override
    protected void setKhoaChinh(DanhMuc danhMuc, ResultSet rs) {
        try {
            if (rs.next()) danhMuc.setMa(rs.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Sach> sachTrongDanhMuc(DanhMuc danhMuc) {
        return null;
    }
}
