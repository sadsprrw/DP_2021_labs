package dao;

import jdbc.DBManager;
import models.StudentsGroup;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentsGroupDAO {
    public static StudentsGroup findById(long id) {
        try (Connection connection = DBManager.getConnection()) {
            String sql =
                    "SELECT * FROM students_group WHERE id = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            StudentsGroup studentsGroup = null;
            if (resultSet.next()) {
                studentsGroup = new StudentsGroup();
                studentsGroup.setId(resultSet.getInt(1));
                studentsGroup.setName(resultSet.getString(2));
            }
            preparedStatement.close();
            return studentsGroup;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static StudentsGroup findByName(String name) {
        try (Connection connection = DBManager.getConnection()) {
            String sql = "SELECT * FROM students_group WHERE name = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            StudentsGroup studentsGroup = null;
            if (resultSet.next()) {
                studentsGroup = new StudentsGroup();
                studentsGroup.setId(resultSet.getInt(1));
                studentsGroup.setName(resultSet.getString(2));
            }
            preparedStatement.close();
            return studentsGroup;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean update(StudentsGroup group) {
        try (Connection connection = DBManager.getConnection()) {
            String sql = "UPDATE students_group SET name = ? WHERE id = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, group.getName());
            preparedStatement.setLong(2, group.getId());
            int result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insert(StudentsGroup group) {
        try (Connection connection = DBManager.getConnection()) {
            String sql = "INSERT INTO students_group (name) VALUES (?)";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, group.getName());
            if(preparedStatement.executeUpdate() > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    group.setId(resultSet.getInt(1));
                } else
                    return false;
                preparedStatement.close();
                System.out.println(group.getId());
                return true;
            }
            DBManager.getInstance().commitAndClose(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delete(StudentsGroup group) {
        try (Connection connection = DBManager.getConnection()) {
            String sql = "DELETE FROM students_group WHERE id = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, group.getId());
            int result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<StudentsGroup> findAll() {
        try (Connection connection = DBManager.getConnection()) {
            String sql = "SELECT * FROM students_group";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<StudentsGroup> list = new ArrayList<>();
            while (resultSet.next()) {
                StudentsGroup studentsGroup = new StudentsGroup();
                studentsGroup.setId(resultSet.getInt(1));
                studentsGroup.setName(resultSet.getString(2));
                list.add(studentsGroup);
            }
            preparedStatement.close();
            DBManager.getInstance().commitAndClose(connection);
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}