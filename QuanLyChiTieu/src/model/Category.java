package model;

public class Category {

    private int categoryId;
    private String name;
    private String type;
    private int userId;

    public Category(int int1, String string, String string2, int int2) {
		// TODO Auto-generated constructor stub
	}

	// Getters
    public int getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getUserId() {
        return userId;
    }

    // Setters
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

