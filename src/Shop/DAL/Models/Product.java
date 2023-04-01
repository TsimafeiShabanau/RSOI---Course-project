package Shop.DAL.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties(value = { "vendorName" })
public class Product
{
    private int id;
    private String name;
    private String description;
    private Double price;
    private Vendor vendor;
    private ArrayList<Category_Product> categories;

    public Product()
    {
    }

    public Product(int id, String name, String description, double price, Vendor vendor, ArrayList<Category_Product> categories)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.vendor = vendor;
        this.categories = categories;
    }

    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public double getPrice()
    {
        return price;
    }
    public void setPrice(double price)
    {
        this.price = price;
    }

    public Vendor getVendor()
    {
        return vendor;
    }
    public void setVendor(Vendor vendor)
    {
        this.vendor = vendor;
    }

    public String getVendorName() {return vendor.getName();}

    public ArrayList<Category_Product> getCategories()
    {
        return categories;
    }

    public void setCategories(ArrayList<Category_Product> categories)
    {
        this.categories = categories;
    }
}