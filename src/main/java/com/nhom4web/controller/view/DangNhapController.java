package com.nhom4web.controller.view;

import com.nhom4web.dao.impl.NguoiDungDAO;
import com.nhom4web.dao.impl.ThongTinDangNhapDAO;
import com.nhom4web.model.NguoiDung;
import com.nhom4web.model.ThongTinDangNhap;
import com.nhom4web.utils.Hashing;
import com.nhom4web.utils.Json;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;

@MultipartConfig
@WebServlet("/api/dang-nhap")
public class DangNhapController extends HttpServlet {
    private static final NguoiDungDAO ND_DAO = new NguoiDungDAO();
    private static final ThongTinDangNhapDAO TTDN_DAO = new ThongTinDangNhapDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LinkedHashMap<String, Object> taiKhoanMatKhau = new LinkedHashMap<>();

        try {
            taiKhoanMatKhau.put("tenDangNhap", req.getParameter("tenDangNhap"));
            String matKhauMaHoa = Hashing.maHoaSHA256(req.getParameter("matKhau"));
            taiKhoanMatKhau.put("matKhau", matKhauMaHoa);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        ThongTinDangNhap thongTinDangNhap = TTDN_DAO.timTheoTaiKhoanMatKhau(taiKhoanMatKhau);
        if (thongTinDangNhap != null) {
            Json.chuyenThanhJson(resp, "Đăng nhập thành công");

            NguoiDung nguoiDung = ND_DAO.tim(thongTinDangNhap.getMaNguoiDung());

            // Tao session dang nhap
            HttpSession sessionDangNhap = req.getSession();
            sessionDangNhap.setAttribute("nguoiDung", nguoiDung);

            /*
             * Phan quyen
             * 1. Nguoi dung co loaiNguoiDung = 0 => User => Redirect den trang chu
             * 2. Nguoi dung co loaiNguoiDung = 1 => Admin => Redirect den trang admin
             */
            if (nguoiDung.getLoaiNguoiDung() == 0) {
                req.getRequestDispatcher("views/trangchu.jsp").forward(req, resp);
            } else {
                req.getRequestDispatcher("views/sach_admin.jsp").forward(req, resp);
            }
            return;
        } else {
            Json.chuyenThanhJson(resp, "Đăng nhập thất bại");
        }

        RequestDispatcher rd = req.getRequestDispatcher("views/dangnhap.jsp");
        rd.forward(req, resp);
    }
}
