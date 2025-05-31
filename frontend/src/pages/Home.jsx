import Card from '../components/Cart';
import FeaturedNews from '../components/FeaturedNews';
import NewsCard from '../components/NewsCard';
import GoldPriceChart from '../components/GoldPriceChart';
import GoldPriceTable from '../tables/GoldPriceTable';
import ExchangeRateTable from '../tables/ExchangeRateTable';
import { Contact } from "../components/contact";
import '../styles/Dashboard.css';
import useArticles from '../hooks/useArticles';

export default function Home({ contactData }) {
  const { articles, loading, error } = useArticles();

  if (loading) return <div>Đang tải...</div>;
  if (error) return <div>Lỗi: {error}</div>;

  return (
    <div className="container" style={{marginTop: '32px'}}>
      <div className="dashboard-grid">
        {articles[0] && (
          <Card style={{gridColumn: 'span 2', minHeight: 250}}>
            <FeaturedNews article={articles[0]} />
          </Card>
        )}
        {articles.slice(1, 19).map((article, idx) => (
          <Card
            key={article.id || idx}
            style={
              (idx % 7 === 0)
                ? { gridColumn: 'span 2', minHeight: 200 }
                : {}
            }
          >
            <NewsCard article={article} />
          </Card>
        ))}
        <Card>
          <GoldPriceChart />
        </Card>
        <Card>
          <GoldPriceTable />
        </Card>
        <Card>
          <ExchangeRateTable />
        </Card>
      </div>
    </div>
  );
}
