export default function Card({ children, style }) {
  return (
    <div
      className="custom-card"
      style={{
        borderRadius: 16,
        boxShadow: "0 2px 8px rgba(0,0,0,0.08)",
        background: "#fff",
        padding: 16,
        marginBottom: 16,
        ...style,
      }}
    >
      {children}
    </div>
  );
}
