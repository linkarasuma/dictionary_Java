package dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Word;

public class WordDAO {
    private final String JDBC_URL = "jdbc:postgresql://localhost:5432/dictionary";
    private final String DB_USER = "postgres";
    private final String DB_PASS = "sql";

    static {
        try {
            // ドライバの初期化
            Class.forName("org.postgresql.Driver");
            System.out.println("ドライバ初期化成功");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Word> searchWords(String searchWord, int limit) {
        List<Word> wordList = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
            System.out.println("データベース接続成功");
            
            // クエリを準備して実行
            String sql = "SELECT WORD, DESCRIPTION FROM en_jp WHERE WORD ILIKE ? LIMIT ?";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, "%" + searchWord + "%");
            pStmt.setInt(2, limit);
            ResultSet rs = pStmt.executeQuery();
            
            // クエリの実行結果を確認
            System.out.println("クエリ実行成功");

            // 結果をデバッグ出力
            while (rs.next()) {
                String word = rs.getString("WORD");
                String description = rs.getString("DESCRIPTION");
                System.out.println("単語: " + word + " | 説明: " + description); // デバッグ用
                wordList.add(new Word(word, description));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wordList;
    }

    public static void main(String[] args) {
        try {
            // ドライバの初期化
            Class.forName("org.postgresql.Driver");
            System.out.println("ドライバ初期化成功");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void loadWords() {
        System.out.println("loadWordsメソッド実行開始"); // デバッグ用メッセージ

        String filePath = "C:/data/ejdic-hand-utf8.txt"; // テキストファイルのパスを指定

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            conn.setAutoCommit(false); // 自動コミットをオフ

            // テーブルの中身をクリア
            String clearSql = "DELETE FROM en_jp";
            PreparedStatement clearStmt = conn.prepareStatement(clearSql);
            clearStmt.executeUpdate();
            System.out.println("テーブルクリア成功");

            System.out.println("データベース接続成功");

            String sql = "INSERT INTO en_jp (word, description) VALUES (?, ?)";
            PreparedStatement pStmt = conn.prepareStatement(sql);

            String line;
            int batchSize = 100; // バッチサイズの設定
            int count = 0;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t"); // タブ区切りの場合
                if (parts.length == 2) {
                    String word = parts[0];
                    String description = parts[1];

                    pStmt.setString(1, word);
                    pStmt.setString(2, description);
                    pStmt.addBatch();

                    if (++count % batchSize == 0) {
                        pStmt.executeBatch(); // バッチごとに実行
                    }
                }
            }

            pStmt.executeBatch(); // 最後に残ったバッチを実行
            conn.commit(); // コミット

            System.out.println("英単語登録成功");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        System.out.println("loadWordsメソッド実行終了"); // デバッグ用メッセージ
    }
}
