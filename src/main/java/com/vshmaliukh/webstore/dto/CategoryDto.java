package com.vshmaliukh.webstore.dto;

import com.vshmaliukh.webstore.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    public static final int NAME_MIN_LENGTH = 3;
    public static final int NAME_MAX_LENGTH = 50;
    public static final int DESCRIPTION_MAX_LENGTH = 450;

    private Integer id;

    @NotBlank(message = "Invalid category 'name': Empty name")
    @NotNull(message = "Invalid category 'name': name is NULL")
    @Size(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH,
            message = "Invalid category 'name': name size min = " + NAME_MIN_LENGTH + ", max = " + NAME_MAX_LENGTH)
    private String name;

    @Size(max = DESCRIPTION_MAX_LENGTH,
            message = "Invalid category 'description': description size max = " + DESCRIPTION_MAX_LENGTH)
    private String description;

    private boolean isArchived = false;

    private boolean isActivated = true;

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.isArchived = category.isArchived();
        this.isActivated = category.isActivated();
    }

}
