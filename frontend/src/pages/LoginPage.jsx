import {useState} from "react";
import {useNavigate} from "react-router-dom";

export default function LoginPage({setUser}) {
  const [form, setForm] = useState({email: "", password: ""});
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({...form, [e.target.name]: e.target.value});
  };

  const handleSubmit = async () => {
    try {
      const res = await fetch(`https://goldenages.online/api/users/login`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(form),
      });

      if (res.ok) {
        const data = await res.json();
        // Lưu token
        localStorage.setItem("token", data.token);

        // Giải mã token và lấy thông tin user
        const payload = JSON.parse(atob(data.token.split(".")[1]));

        // Lưu thông tin user vào localStorage
        localStorage.setItem("user", JSON.stringify({
          email: payload.sub,
          role: payload.role
        }));

        // Set user state
        setUser({email: payload.sub, role: payload.role});

        // Chuyển hướng về trang chủ
        navigate("/");

        // Refresh để cập nhật navigation
        window.location.reload();
      } else {
        alert("Login failed");
      }
    } catch (error) {
      console.error("Login error:", error);
      alert("Login failed. Please try again.");
    }
  };

  return (
    <div className="container" style={{ marginTop: '100px' }}>
      <div className="flex flex-col gap-2">
        <input
          type="email"
          name="email"
          placeholder="Email"
          onChange={handleChange}
          className="border p-2"
        />
        <input
          type="password"
          name="password"
          placeholder="Password"
          onChange={handleChange}
          className="border p-2"
        />
        <button onClick={handleSubmit} className="bg-green-500 text-white p-2 rounded">
          Login
        </button>
        <p>
          Don't have an account? <a href="/register" className="text-blue-500">Register</a>
        </p>
      </div>
    </div>
  );
}
