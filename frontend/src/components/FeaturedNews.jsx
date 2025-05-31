export default function FeaturedNews({ article }) {
  if (!article) return null;
  return (
    <div>
      {article.mediaType === 'video' ? (
        <video
          style={{width: '100%', maxHeight: 250, objectFit: 'cover', borderRadius: 12}}
          controls
        >
          <source src={article.mediaUrl} type="video/mp4" />
          Trình duyệt của bạn không hỗ trợ video.
        </video>
      ) : (
        <img
          src={article.mediaUrl}
          alt={article.title}
          style={{width: '100%', maxHeight: 250, objectFit: 'cover', borderRadius: 12}}
        />
      )}
      <h3 style={{marginTop: 12}}>{article.title}</h3>
      <p>{article.description}</p>
      <small>{new Date(article.dateTime).toLocaleString()}</small>
    </div>
  );
} 