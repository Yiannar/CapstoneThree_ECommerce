package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao implements CategoryDao
{
    private DataSource dataSource;

    @Autowired
    public MySqlCategoryDao(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    @Override
    public List<Category> getAllCategories(String name, String description, int category_id) {
        List<Category> categories = new ArrayList<>();

        String query = "SELECT * FROM categories WHERE name LIKE ? AND description LIKE ? AND (category_id = ? OR ? = -1)";

        int categoryToSearch = category_id == 0 ? -1 : category_id;
        String nameToSearch = name == null ? "" : name;
        String descriptionToSearch = description == null ? "" : description;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setString(1, "%" + nameToSearch + "%");
            preparedStatement.setString(2, "%" + descriptionToSearch + "%");
            preparedStatement.setInt(3, categoryToSearch);
            preparedStatement.setInt(4, categoryToSearch);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Category category = mapRow(resultSet);
                categories.add(category);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // get all categories
        return categories;
    }

    @Override
    public List<Category> getAllCategories(String name, String description) {
        List<Category> categories = new ArrayList<>();

        String query = "SELECT * FROM categories WHERE name LIKE ? AND description LIKE ?";

        String nameToSearch = name == null ? "" : name;
        String descriptionToSearch = description == null ? "" : description;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            preparedStatement.setString(1, "%" + nameToSearch + "%");
            preparedStatement.setString(2, "%" + descriptionToSearch + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Category category = mapRow(resultSet);
                categories.add(category);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // get all categories
        return categories;
    }


    @Override
    public Category getById(int categoryId)
    {
        // get category by id

        Category category = null;
        String query = "SELECT * FROM categories WHERE category_id=?";

        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ){
            preparedStatement.setInt(1, categoryId);

            try(
                    ResultSet resultSet = preparedStatement.executeQuery();
                    ){
                while (resultSet.next()){
                    category = mapRow(resultSet);
                    break;
                }
            }
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        return category;
    }

    @Override
    public Category create(Category category)
    {
        // create a new category
        return null;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        // update category
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}