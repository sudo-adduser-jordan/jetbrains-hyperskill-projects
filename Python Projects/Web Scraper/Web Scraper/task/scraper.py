import string
import requests
import os
from bs4 import BeautifulSoup
# https://www.nature.com/nature/articles?sort=PubDate&year=2020&page=3


def save_article(article_link, current_page):
    link = "https://www.nature.com" + article_link
    r = requests.get(link,
                     headers={"Accept-Language": "en-US;q=0.5"})
    soup = BeautifulSoup(r.content, "html.parser")
    title = soup.find("title").text
    title = title.translate(str.maketrans("", "", string.punctuation + "’"))
    title = title.replace(" ", "_")
    page_body = soup.find_all("div", {"class": "c-article-body"})
    full_path = os.path.join(os.getcwd(), "Page_" + str(current_page), title)
    file = open("{}.txt".format(full_path), "wb")
    file.write(bytearray(page_body[0].text, "utf-8"))
    file.close()


def scrap_page(pages_to_search: int, article_type: string):

    for page in range(1, pages_to_search+1):
        url_address = "https://www.nature.com/nature/articles?sort=PubDate&year=2020" + "&page=" + str(page)
        r = requests.get(url_address)
        soup = BeautifulSoup(r.content, "html.parser")

        if not os.path.exists(os.path.join(os.getcwd(), "Page_" + str(page))):
            os.mkdir(os.path.join(os.getcwd(), "Page_" + str(page)))

        for article in soup.find_all("article"):
            if article.find("span", {"data-test": "article.type"}).text == ("\n" + article_type + "\n"):
                save_article(article.find("a")["href"], page)
    print("Saved all articles.")


scrap_page(int(input("Pages to search: ")), str(input("What type of article to search for: ")))
