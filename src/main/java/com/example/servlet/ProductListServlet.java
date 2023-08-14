package com.example.servlet;

import com.example.entity.Product;
import com.example.util.HibernateUtil;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProductListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = Integer.parseInt(req.getParameter("page"));
        int pageSize = 10; // Number of products per page

        try (Session session = HibernateUtil.getSession()) {
            List<Product> productList = session.createQuery("FROM Product", Product.class)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .list();

            req.setAttribute("productList", productList);

            // Calculate total pages (this is just a simple example, you might need a more sophisticated approach)
            long totalProducts = (long) session.createQuery("SELECT COUNT(*) FROM Product").uniqueResult();
            int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
            req.setAttribute("totalPages", totalPages);

            req.getRequestDispatcher("productList.jsp").forward(req, resp);
        }
    }
}
