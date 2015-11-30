package com.similarwords.app

import org.scalatra._
import scalate.ScalateSupport

// JSON handling support from Scalatra
import org.scalatra.json._

// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}

import org.scalatra.CorsSupport



class SimilarWordsServlet extends SimilarWordsAppStack with JacksonJsonSupport with CorsSupport {

    protected implicit lazy val jsonFormats: Formats = DefaultFormats

    // Initialize Trie structure
    val trie = new Trie
    trie.load("enable1.txt")

    get("/") {
        <html>
            <body>
                <h1>Hello!</h1>
            </body>
        </html>
    }

    options("/similar/*") {
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
    }

    get("/similar/:prefix") {
        contentType = formats("json")
        // Get words with same prefix
        trie.similar({params("prefix")}.toLowerCase())
    }
}
