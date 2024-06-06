package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.repository.UserRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReviewService;


@Controller
@RequestMapping("/houses/{houseId}/reviews")
public class ReviewController {
	private final ReviewRepository reviewRepository;
    private final HouseRepository houseRepository;
    private final UserRepository userRepository;
    private final ReviewService reviewService;

    public ReviewController (ReviewRepository reviewRepository, HouseRepository houseRepository, UserRepository userRepository, ReviewService reviewService){
        this.reviewRepository = reviewRepository;
        this.houseRepository = houseRepository;
        this.userRepository = userRepository;
        this.reviewService = reviewService;
    }

    /* レビュー一覧表示 */
    @GetMapping
    public String index(@PathVariable("houseId") Integer houseId, Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findByHouseId(houseId, pageable);
        House house = houseRepository.getReferenceById(houseId);
        model.addAttribute("reviewPage", reviewPage);
        model.addAttribute("house", house);
        return "reviews/index";
    }

    /* レビュー投稿画面表示 */
    @GetMapping("/register")
    public String register (@PathVariable("houseId") Integer houseId, Model model) {
        House house = houseRepository.getReferenceById(houseId);
        model.addAttribute("reviewRegisterForm", new ReviewRegisterForm());
        model.addAttribute("house", house);
        return "reviews/register";
    }

    /* レビュー投稿機能 */
    @PostMapping("/create")
    public String create (@PathVariable("houseId") Integer houseId, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reviews/register";
        }
        User user = userDetailsImpl.getUser();
        reviewService.create(houseId, reviewRegisterForm, user);
        redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");
        return "redirect:/houses/" + houseId;
    }

    /* レビュー編集画面表示 */
    @GetMapping("/{reviewId}/edit")
    public String edit(@PathVariable(name = "houseId") Integer houseId, @PathVariable(name = "reviewId") Integer reviewId, Model model) {
        Review review = reviewRepository.getReferenceById(reviewId);
        House house = houseRepository.getReferenceById(houseId);
        ReviewEditForm reviewEditForm = new ReviewEditForm(review.getId(), review.getRating(), review.getComment());
        model.addAttribute("reviewEditForm" ,reviewEditForm);
        model.addAttribute("review", review);
        model.addAttribute("house", house);
        return "reviews/edit";
    }

    /* レビュー更新機能 */
    @PostMapping("/{reviewId}/update")
    public String update(@PathVariable("houseId") Integer houseId, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @ModelAttribute @Validated ReviewEditForm reviewEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            return "reviews/edit";
        }
        User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
        reviewService.update(houseId, reviewEditForm, user);
        redirectAttributes.addFlashAttribute("successMessage", "レビューを編集しました。");
        return "redirect:/houses/" + houseId;
    }

    /* レビュー削除機能 */
    @PostMapping("/{reviewId}")
    public String delete(@PathVariable("houseId") Integer houseId, @PathVariable("reviewId") Integer reviewId) {
       reviewService.delete(reviewId);
       return "redirect:/houses/" + houseId;
    }
}
