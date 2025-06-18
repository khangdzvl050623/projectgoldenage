import React, { useState } from "react";
import Card from '../components/Cart';
import FeaturedNews from '../components/FeaturedNews';
import NewsCard from '../components/NewsCard';
import useArticles from '../hooks/useArticles';
import '../styles/Dashboard.css';
import '../styles/NewsPage.css';
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";


const getCategoryFromImageUrl = (url) => {
  if (!url) return 'Khác';
  if (url.includes("dulich")) return "Du lịch";
  if (url.includes("giaitri")) return "Giải trí";
  if (url.includes("suckhoe")) return "Sức khỏe";
  if (url.includes("thethao")) return "Thể thao";
  if (url.includes("vnexpress")) return "Thời sự";
  return "Khác";
};

const publishers = [
  { name: 'VnExpress', logo: 'https://upload.wikimedia.org/wikipedia/commons/4/4e/VnExpress.net_logo.png' },
  { name: 'VTC News' },
  { name: 'Tiền Phong' },
  { name: 'Lao Động' },
  { name: 'Tuổi trẻ' },
  { name: 'VietnamPlus' },
];

const NewsPage = () => {
  const { articles, loading, error } = useArticles();

  const [currentPage, setCurrentPage] = useState(1);
  const articlesPerPage = 10;
  const maxPages = 3;
  const maxArticlesForPagination = articlesPerPage * maxPages;

  const articlesToPaginate = articles.slice(0, maxArticlesForPagination);
  const indexOfLastArticle = currentPage * articlesPerPage;
  const indexOfFirstArticle = indexOfLastArticle - articlesPerPage;
  const currentArticles = articlesToPaginate.slice(indexOfFirstArticle, indexOfLastArticle);
  const totalPages = maxPages;

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
    const newsListElement = document.querySelector('.news-list');
    if (newsListElement) {
      newsListElement.scrollIntoView({ behavior: 'smooth' });
    }
  };

  const renderPaginationButtons = () => {
    const pageNumbers = [];
    for (let i = 1; i <= totalPages; i++) {
      pageNumbers.push(i);
    }

    return (
      <div className="pagination">
        {pageNumbers.map(number => (
          <button
            key={number}
            onClick={() => handlePageChange(number)}
            className={currentPage === number ? 'active' : ''}
          >
            {number}
          </button>
        ))}
      </div>
    );
  };

  if (loading) return <div>Đang tải...</div>;
  if (error) return <div>Lỗi: {error}</div>;

  return (
    <div className="news-layout">
      <aside className="news-sidebar">
        <div className="sidebar-logo"></div>
        <h2 className="sidebar-title">Tin tức</h2>
        <div className="sidebar-publishers">
          <div className="sidebar-follow-title">Các nhà phát hành đã theo dõi</div>
          <ul>
            <li>VnExpress</li>
          </ul>
          <div className="sidebar-suggest-title">Nhà phát hành được đề xuất</div>
          <ul>
            {publishers.slice(1).map(pub => <li key={pub.name}>{pub.name} <button>+</button></li>)}
          </ul>
        </div>
      </aside>

      <main className="news-main">
        <section className="top-stories">
          <h3>Top Stories</h3>
          <div className="top-stories-slider">
            <Slider
              dots={true}
              infinite={true}
              speed={500}
              slidesToShow={1}
              slidesToScroll={1}
              arrows={true}
            >
              {articles.slice(0, 3).map((article, idx) => (
                <div key={article.id || idx} style={{ padding: 8 }}>
                  <FeaturedNews article={article} />
                </div>
              ))}
            </Slider>
          </div>
        </section>

        <section className="news-list">
          {currentArticles.map((article, idx) => (
            <Card key={article.id || idx} style={{ marginBottom: 24 }}>
              <NewsCard
                article={article}
                category={getCategoryFromImageUrl(article.mediaUrl || article.link || "")}
              />
            </Card>
          ))}
        </section>

        {totalPages > 1 && renderPaginationButtons()}
      </main>
    </div>
  );
};

export default NewsPage;
