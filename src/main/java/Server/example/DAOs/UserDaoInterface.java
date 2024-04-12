package Server.example.DAOs;

/** OOP Feb 2022
 * UserDaoInterface
 *
 * Declares the methods that all UserDAO types must implement,
 * be they MySql User DAOs or Oracle User DAOs etc...
 *
 * Classes from the Business Layer (users of this DAO interface)
 * should use reference variables of this interface type to avoid
 * dependencies on the underlying concrete classes (e.g. MySqlUserDao).
 *
 * More sophisticated implementations will use a factory
 * method to instantiate the appropriate DAO concrete classes
 * by reading database configuration information from a
 * configuration file (that can be changed without altering source code)
 *
 * Interfaces are also useful when testing, as concrete classes
 * can be replaced by mock DAO objects.
 */

import Server.example.DTOs.Book;
import Server.example.Exceptions.DaoException;
import java.util.List;

public interface UserDaoInterface
{
    public List<Book> findAllBooks() throws DaoException;

    public Book getBookByID(int id) throws DaoException;

    public void insertBook(int id, String title, String author, float price) throws DaoException;

    public Book deleteBookByID(int id) throws DaoException;

    public void updateBookByID(int id, String title, String author, float price) throws DaoException;


    List<Book> getBookByFilter(Book filter);


}

