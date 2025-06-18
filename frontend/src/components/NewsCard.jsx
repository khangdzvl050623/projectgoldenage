export default function NewsCard({ article, category }) {
  if (!article) return null;

  return (
    <div
      style={{ cursor: 'pointer' }}
      onClick={() => window.open(article.link, "_blank")}
    >
      {/* Hiển thị chủ đề nếu có */}
      {category && (
        <span
          style={{
            display: 'inline-block',
            backgroundColor: '#f0f0f0',
            color: '#333',
            fontSize: 12,
            padding: '2px 6px',
            borderRadius: 12,
            marginBottom: 4
          }}
        >
          {category}
        </span>
      )}

      {article.mediaType === 'video' ? (
        <video
          style={{ width: '100%', height: 120, objectFit: 'cover', borderRadius: 8 }}
          controls
        >
          <source src={article.mediaUrl} type="video/mp4" />
          Trình duyệt của bạn không hỗ trợ video.
        </video>
      ) : (
        <img
          src={article.mediaUrl}
          alt={article.title}
          style={{ width: '100%', height: 120, objectFit: 'cover', borderRadius: 8 }}
        />
      )}

      <h5 style={{ marginTop: 8, fontSize: 16 }}>{article.title}</h5>
      <p className="text-truncate">{article.description}</p>
      <small>{new Date(article.dateTime).toLocaleString()}</small>
    </div>
  );
}
