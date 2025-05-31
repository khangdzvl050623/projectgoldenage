import "./styles/App.css";
import "./styles/images.css";
import {useState, useEffect} from "react";
import {Routes, Route} from "react-router-dom";
import GoldPriceTable from "./tables/GoldPriceTable";
import ExchangeRateTable from "./tables/ExchangeRateTable";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import {Navigation} from "./components/navigation";
import {Contact} from "./components/contact";
import JsonData from "./data/data.json";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min";
import {ErrorBoundary} from 'react-error-boundary';
import GoldPriceChart from "./components/GoldPriceChart";
import NewsPage from "./pages/NewsPage";
import Home from "./pages/Home";

function ErrorFallback({error}) {
  return (
    <div>
      <h2>Something went wrong:</h2>
      <pre>{error.message}</pre>
    </div>
  )
}

export default function App() {
  const [user, setUser] = useState(null);
  const [landingPageData, setLandingPageData] = useState({});

  useEffect(() => {
    setLandingPageData(JsonData);
    const token = localStorage.getItem("token");
    if (token) {
      const payload = JSON.parse(atob(token.split(".")[1]));
      setUser({email: payload.sub, role: payload.role});
    }
  }, []);

  return (
    <ErrorBoundary FallbackComponent={ErrorFallback}>
      <div>
        <Navigation/>
        <Routes>
          <Route path="/" element={<Home contactData={landingPageData.Contact}/>} />
          <Route path="/exchangerate" element={
            <div className="container" style={{marginTop: '100px'}}>
              <h2 className="text-center mb-4">Tỷ Giá Ngoại Tệ</h2>
              <ExchangeRateTable/>
            </div>
          }/>
          <Route path="/goldprice" element={
            <div className="container" style={{marginTop: '100px'}}>
              <h2 className="text-center mb-4">Giá Vàng</h2>
              <GoldPriceTable/>
              <GoldPriceChart/>
            </div>
          }/>
          <Route path="/login" element={<LoginPage setUser={setUser}/>}/>
          <Route path="/register" element={<RegisterPage setUser={setUser}/>}/>
          <Route path="*" element={<h1>404 - Page Not Found</h1>}/>
        </Routes>
      </div>
    </ErrorBoundary>
  );
}
