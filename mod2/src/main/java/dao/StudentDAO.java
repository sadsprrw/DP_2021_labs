package dao;

import jdbc.DBManager;
import models.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public static Student findById(long id) {
        try (Connection connection = DBManager.getConnection();) {
            String sql = "SELECT * FROM Student WHERE id = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Student student = null;
            if (resultSet.next()) {
                student = new Student();
                student.setId(resultSet.getInt(1));
                student.setGroupId(resultSet.getInt(2));
                student.setName(resultSet.getString(3));
                student.setGroupId(resultSet.getInt(4));
            }
            preparedStatement.close();
            return student;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean update(Student student) {
        try (Connection connection = DBManager.getConnection();) {
            String sql = "UPDATE Student SET age = ?, name = ?, group_id = ?, WHERE id = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setShort(1, student.getAge());
            preparedStatement.setString(2, student.getName());
            preparedStatement.setInt(3, student.getGroupId());
            preparedStatement.setInt(4, student.getId());
            int result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insert(Student student) {
        try (Connection connection = DBManager.getConnection();) {
            String sql = "INSERT INTO Student (age, name, group_id) VALUES (?,?,?)";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setShort(1, student.getAge());
            preparedStatement.setString(2, student.getName());
            preparedStatement.setInt(3, student.getGroupId());
            if (preparedStatement.executeUpdate() > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    student.setId(resultSet.getInt(1));
                } else
                    return false;
                preparedStatement.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delete(Student student) {
        try (Connection connection = DBManager.getConnection();) {
            String sql = "DELETE FROM Student WHERE id = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, student.getId());
            int result = preparedStatement.executeUpdate();
            preparedStatement.close();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Student> findAll() {
        try (Connection connection = DBManager.getConnection();) {
            String sql = "SELECT * FROM Student";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Student> list = new ArrayList<>();
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt(1));
                student.setAge(resultSet.getShort(2));
                student.setName(resultSet.getString(3));
                student.setGroupId(resultSet.getInt(4));
                list.add(student);
            }
            preparedStatement.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Student> findByAirCompanyId(Long id) {
        try (Connection connection = DBManager.getConnection();) {
            String sql = "SELECT * FROM Student WHERE group_id = ?";
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Student> list = new ArrayList<>();
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt(1));
                student.setAge(resultSet.getShort(2));
                student.setName(resultSet.getString(3));
                student.setGroupId(resultSet.getInt(4));
                list.add(student);
            }
            preparedStatement.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}