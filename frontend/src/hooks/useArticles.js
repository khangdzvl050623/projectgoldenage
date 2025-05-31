import { useState, useEffect } from 'react';
import axios from 'axios';

export default function useArticles() {
  const [articles, setArticles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const cached = localStorage.getItem('articles');
    if (cached) {
      setArticles(JSON.parse(cached));
      setLoading(false);
    }
    async function fetchArticles() {
      try {
        const response = await axios.get('https://goldenages.online/api/scrape/history');
        setArticles(response.data);
        localStorage.setItem('articles', JSON.stringify(response.data));
        setLoading(false);
      } catch (err) {
        setError('Failed to fetch articles');
        setLoading(false);
      }
    }
    fetchArticles();
  }, []);

  return { articles, loading, error };
}
