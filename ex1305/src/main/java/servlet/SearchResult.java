package servlet;

import java.io.IOException;
import java.util.List;

import dao.WordDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Word;

@WebServlet("/SearchResult")
public class SearchResult extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // リクエストパラメータの取得
        String word = request.getParameter("word");
        String limitParam = request.getParameter("limit");
        int limit = (limitParam != null && !limitParam.isEmpty()) ? Integer.parseInt(limitParam) : 10;

        System.out.println("検索する英単語: " + word);
        System.out.println("最大表示件数: " + limit);

        // 検索結果を取得
        WordDAO wordDAO = new WordDAO();
        List<Word> wordList = wordDAO.searchWords(word, limit);

        // 検索結果をリクエストスコープに保存
        request.setAttribute("wordList", wordList);
        // 検索文字列と表示件数をリクエストスコープに保存
        request.setAttribute("searchWord", word);
        request.setAttribute("searchLimit", limit);

        // 結果表示ページにフォワード
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/searchResult.jsp");
        dispatcher.forward(request, response);
    }
}
