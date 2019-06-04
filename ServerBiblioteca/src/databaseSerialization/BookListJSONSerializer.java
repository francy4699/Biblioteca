package databaseSerialization;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;

import databaseManagement.DatabaseConnection;

public class BookListJSONSerializer {

	private Gson gson;
	private ArrayList<String> jsonStringArray;

	public BookListJSONSerializer(DatabaseConnection databaseConnection) throws SQLException {
		gson = new Gson();
		jsonStringArray = new ArrayList<String>();
		
		ResultSet categoriesResultSet = databaseConnection.getCategories();
		while (categoriesResultSet.next()) {
			Category c = new Category(categoriesResultSet.getString("Category"));
			ResultSet booksResultSet = databaseConnection
					.getBooksFromCategory(categoriesResultSet.getInt("Category_ID"));
			ArrayList<Book> books = new ArrayList<Book>();
			while(booksResultSet.next())
				books.add(new Book(booksResultSet.getString("Title"), 
						booksResultSet.getString("Author"),
						booksResultSet.getInt("Num_of_pages"),
						booksResultSet.getString("Publisher"),
						booksResultSet.getString("Language"),
						booksResultSet.getString("Description"),
						booksResultSet.getInt("ISBN"),
						booksResultSet.getInt("Num_of_books")));
			c.setBooks(books.toArray(new Book[books.size()]));
			jsonStringArray.add(gson.toJson(c));
		}
	}

	public ArrayList<String> getJSONStringArray() {
		return jsonStringArray;
	}
}
