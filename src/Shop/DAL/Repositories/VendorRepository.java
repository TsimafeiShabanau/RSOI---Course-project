package Shop.DAL.Repositories;

import Shop.DAL.Interfaces.IVendorRepository;
import Shop.DAL.Models.Vendor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VendorRepository extends GenericRepository<Vendor> implements IVendorRepository
{
    public VendorRepository(String dbSet, Connection db)
    {
        super(dbSet, db);
    }

    @Override
    public void Create(Vendor item) throws SQLException
    {
        query = "INSERT INTO " + dbSet + " (VendorName, Email, PhoneNumber, Country) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = db.prepareStatement(query);
        ps.setString(1, item.getName());
        ps.setString(2, item.getEmail());
        ps.setString(3, item.getPhoneNumber());
        ps.setString(4, item.getCountry());
        ps.executeUpdate();
    }

    @Override
    public void Update(Vendor item) throws SQLException
    {
        query = "UPDATE " + dbSet + " SET VendorName = ?, Email = ?, PhoneNumber = ?, Country = ? WERE VendorId =" + item.getId();
        PreparedStatement ps = db.prepareStatement(query);
        ps.setString(1, item.getName());
        ps.setString(1, item.getEmail());
        ps.setString(1, item.getPhoneNumber());
        ps.setString(1, item.getCountry());
        ps.executeUpdate();
    }

    @Override
    public List<Vendor> ProcessData(ResultSet result) throws SQLException
    {
        List<Vendor> data = new ArrayList<>();
        while (result.next())
        {
            data.add(new Vendor(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5)));
        }
        return data;
    }
}
