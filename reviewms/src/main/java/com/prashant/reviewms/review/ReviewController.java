package com.prashant.reviewms.review;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam Long companyId){
        return new ResponseEntity<>(reviewService.getAllReviews(companyId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addReview(@RequestParam Long companyId, @RequestBody Review review){
        if(reviewService.addReview(companyId,review))
            return new ResponseEntity<>("Review added successfully",HttpStatus.OK);
        else
            return new ResponseEntity<>("Company Not Found to add Review",HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable Long reviewId){

        return new ResponseEntity<>(reviewService.getReview(reviewId),HttpStatus.OK);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable Long reviewId,
                                               @RequestBody Review review){

        boolean isReviewUpdated = reviewService.updateReview(reviewId, review);
        if(isReviewUpdated)
            return new ResponseEntity<>("Review updated successfully",HttpStatus.OK);
        else
            return new ResponseEntity<>("Review not found",HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId){
        boolean isReviewDeleted = reviewService.DeleteReview(reviewId);
        if(isReviewDeleted)
            return new ResponseEntity<>("Review updated successfully",HttpStatus.OK);
        else
            return new ResponseEntity<>("Review not found",HttpStatus.NOT_FOUND);

    }
}
