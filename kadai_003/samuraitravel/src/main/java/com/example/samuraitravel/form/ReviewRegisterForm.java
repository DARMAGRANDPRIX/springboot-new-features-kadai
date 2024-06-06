package com.example.samuraitravel.form;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewRegisterForm{
    @NotNull (message = "評価を選択してください")
    @Range (min = 1, max = 5, message = "評価は1～5の間で選択してください" )
    private Integer rating;

    @Size(max = 255, message = "コメントは255文字以内で入力してください")
    private String comment;
}
