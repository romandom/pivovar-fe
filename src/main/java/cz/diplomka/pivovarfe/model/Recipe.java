package cz.diplomka.pivovarfe.model;


import lombok.Data;

import java.util.List;

@Data
public class Recipe {

    private Long id;

    private String name;

    private List<RecipeStep> steps;

}

