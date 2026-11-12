package com.example.skillnest.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PortalTemplateController {

    // Dữ liệu mẫu cho Gói tư vấn / Workshop (MVC-04)
    public record PackageDTO(
            int id, String name, String category, String consultant,
            double price, int duration, boolean active, String image, String description
    ) {}

    public record ConsultantDTO(int id, String fullName, String email, String specialty, String avatar) {}

    private List<PackageDTO> getMockPackages() {
        return List.of(
            new PackageDTO(1, "Định hướng Lộ trình Java Fullstack & AI", "IT & Phần mềm", "ThS. Nguyễn Minh Nhật", 350000, 90, true, "https://images.unsplash.com/photo-1522071820081-009f0129c71c?w=600", "Tư vấn 1-1 xây dựng lộ trình học tập Spring Boot, Microservices, chuẩn bị CV và phỏng vấn thực tập doanh nghiệp."),
            new PackageDTO(2, "Thiết kế UI/UX & Xây dựng Portfolio Nghệ thuật", "Thiết kế & Sáng tạo", "Chuyên gia Lan Khuê", 280000, 60, true, "https://images.unsplash.com/photo-1561070791-2526d30994b5?w=600", "Đánh giá Portfolio cá nhân, hướng dẫn tư duy thiết kế Design System và định hướng nghề nghiệp Product Designer."),
            new PackageDTO(3, "Kỹ năng Phỏng vấn & Tiếng Nhật IT (BrSE)", "Ngoại ngữ & Kỹ năng", "TS. Trần Bảo Châu", 450000, 120, true, "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?w=600", "Luyện phỏng vấn giả định (Mock Interview) và lộ trình chinh phục công việc kỹ sư cầu nối tại Nhật Bản."),
            new PackageDTO(4, "Workshop Nghệ thuật Gốm & Cân bằng Cảm xúc", "Kỹ năng mềm", "Nghệ nhân Hoàng Nam", 320000, 90, false, "https://images.unsplash.com/photo-1565193566173-7a0ee3dbe261?w=600", "Buổi thực hành thủ công kết hợp tư vấn quản lý căng thẳng học tập và định hướng phát triển bản thân cho sinh viên.")
        );
    }

    private List<ConsultantDTO> getMockConsultants() {
        return List.of(
            new ConsultantDTO(1, "ThS. Nguyễn Minh Nhật", "nhatnm@skillnest.edu.vn", "Kiến trúc sư Phần mềm / Java", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300"),
            new ConsultantDTO(2, "Chuyên gia Lan Khuê", "khuel@skillnest.edu.vn", "UI/UX & Art Director", "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=300"),
            new ConsultantDTO(3, "TS. Trần Bảo Châu", "chautb@skillnest.edu.vn", "Cố vấn Nghề nghiệp Quốc tế", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300")
        );
    }

    // 1. TRANG CHỦ, GIỚI THIỆU, TRỢ GIÚP (MVC-02)
    @GetMapping({"/", "/home/index"})
    public String homeIndex(Model model) {
        model.addAttribute("systemName", "SkillNest CareerPath Portal");
        model.addAttribute("shortDesc", "Hệ thống hỗ trợ sinh viên đặt lịch tư vấn học tập, workshop kỹ năng và định hướng nghề nghiệp");
        model.addAttribute("packages", getMockPackages());
        model.addAttribute("consultants", getMockConsultants());
        model.addAttribute("totalPackages", 18);
        model.addAttribute("totalBookings", 245);
        return "home/index";
    }

    @GetMapping("/home/about")
    public String homeAbout() {
        return "home/about";
    }

    @GetMapping("/home/help")
    public String homeHelp() {
        return "home/help";
    }

    // 2. DANH SÁCH & CHI TIẾT GÓI TƯ VẤN (MVC-03 & MVC-04)
    @GetMapping({"/consultation/list", "/consultation/search", "/consultation/category/{id}", "/consultation/search-price"})
    public String consultationList(Model model) {
        model.addAttribute("packages", getMockPackages());
        model.addAttribute("categories", List.of("IT & Phần mềm", "Thiết kế & Sáng tạo", "Ngoại ngữ & Kỹ năng", "Kỹ năng mềm"));
        return "consultation/list";
    }

    @GetMapping("/consultation/detail/{id}")
    public String consultationDetail(@PathVariable("id") int id, Model model) {
        PackageDTO pkg = getMockPackages().stream().filter(p -> p.id() == id).findFirst().orElse(getMockPackages().get(0));
        model.addAttribute("pkg", pkg);
        model.addAttribute("relatedPackages", getMockPackages().stream().filter(p -> p.id() != pkg.id()).limit(3).toList());
        return "consultation/detail";
    }

    // 3. GIỎ ĐẶT LỊCH & FORM ĐẶT LỊCH (MVC-05 & CART-01)
    @GetMapping({"/cart/index", "/cart/add/{id}"})
    public String cartIndex(Model model) {
        model.addAttribute("cartItems", getMockPackages().subList(0, 2));
        model.addAttribute("consultants", getMockConsultants());
        model.addAttribute("totalAmount", 630000);
        return "booking/cart";
    }

    @GetMapping("/booking/form")
    public String bookingForm(Model model) {
        model.addAttribute("packages", getMockPackages());
        model.addAttribute("consultants", getMockConsultants());
        return "booking/form";
    }

    @PostMapping("/booking/save")
    public String bookingSave() {
        return "redirect:/booking/list";
    }

    @GetMapping("/booking/list")
    public String bookingList(Model model) {
        model.addAttribute("packages", getMockPackages());
        return "booking/list";
    }

    // 4. TÀI KHOẢN & QUẢN TRỊ ADMIN (ACCOUNT-01 & DATA-04)
    @GetMapping({"/auth/login", "/account/sign-up", "/account/profile"})
    public String loginPage() {
        return "account/login";
    }

    @GetMapping({"/admin", "/admin/package/index", "/admin/category/index", "/admin/report"})
    public String adminDashboard(Model model) {
        model.addAttribute("packages", getMockPackages());
        model.addAttribute("consultants", getMockConsultants());
        return "admin/index";
    }

    @GetMapping("/system/status")
    @ResponseBody
    public Map<String, Object> systemStatus() {
        return Map.of("system", "SkillNest CareerPath Portal", "status", "ONLINE", "phase", "Giai đoạn 1 - Giao diện Template");
    }
}