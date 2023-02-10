package com.vshmaliukh.webstore.dto;

import com.vshmaliukh.webstore.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Integer id;

    @Size(min = 3, max = 50, message = "Category name size require: min = 3, max = 50")
    private String name;

    @Size(max = 450, message = "Category description size require: max = 450")
    private String description;

    private boolean isArchived = false;

    private boolean isActivated = true;

    public CategoryDto(Category category){
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.isArchived = category.isArchived();
        this.isActivated = category.isActivated();
    }

}
