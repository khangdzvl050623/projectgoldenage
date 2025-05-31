// RegisterPage.js
import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

export default function RegisterPage({ setUser }) {
  const [form, setForm] = useState({ name: "", email: "", password: "" });
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async () => {
    try {
      const res = await fetch(`https://goldenages.online/api/users/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });
      if (res.ok) {
        const data = await res.json();
        localStorage.setItem("token", data.token);
        const payload = JSON.parse(atob(data.token.split(".")[1]));
        setUser({ email: payload.sub, role: payload.role });
        navigate("/");
      } else {
        const errorData = await res.json();
        alert(errorData.message || "Registration failed");
      }
    } catch (error) {
      console.error("Registration error:", error);
      alert("Registration failed. Please try again.");
    }
  };

  return (
    <div className="container" style={{ marginTop: '100px' }}>
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-body">
              <h2 className="text-center mb-4">Register</h2>
              <div className="form-group mb-3">
                <input
                  type="text"
                  name="name"
                  placeholder="Full Name"
                  value={form.name}
                  onChange={handleChange}
                  className="form-control"
                />
              </div>
              <div className="form-group mb-3">
                <input
                  type="email"
                  name="email"
                  placeholder="Email"
                  value={form.email}
                  onChange={handleChange}
                  className="form-control"
                />
              </div>
              <div className="form-group mb-3">
                <input
                  type="password"
                  name="password"
                  placeholder="Password"
                  value={form.password}
                  onChange={handleChange}
                  className="form-control"
                />
              </div>
              <button
                onClick={handleSubmit}
                className="btn btn-primary w-100 mb-3"
              >
                Register
              </button>
              <p className="text-center">
                Already have an account? {' '}
                <Link to="/login" className="text-primary">
                  Login
                </Link>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
