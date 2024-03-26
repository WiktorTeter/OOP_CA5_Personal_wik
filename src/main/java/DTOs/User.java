package DTOs;

/**                                                     OOP Feb 2022
 *  Data Transfer Object (DTO)
 *
 * This POJO (Plain Old Java Object) is called the Data Transfer Object (DTO).
 * (or, alternatively, the Model Object or the Value Object).
 * It is used to transfer data between the DAO and the Business Objects.
 * Here, it represents a row of data from the User database table.
 * The DAO creates and populates a User object (DTO) with data retrieved from
 * the resultSet and passes the User object to the Business Layer.
 *
 * Collections of DTOs( e.g. ArrayList<User> ) may also be passed
 * between the Data Access Layer (DAOs) and the Business Layer objects.
 */

public class User
{
    private int id;
    private String title;
    private String author;
    private Float price;


    public User(int id, String title, String author, Float price)
    {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
    }


    public User( String title, String author, Float price)
    {
        this.id = 0;
        this.title = title;
        this.author = author;
        this.price = price;
    }


    public User()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Float getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = Float.valueOf(price);
    }

    @Override
    public String toString()
    {
        return "User{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                '}';
    }

}
