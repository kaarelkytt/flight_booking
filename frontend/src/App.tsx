import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import FlightList from "./components/FlightList";
import SeatMap from "./components/SeatMap";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<FlightList />} />
                <Route path="/seats/:flightId" element={<SeatMap />} />
            </Routes>
        </Router>
    );
}

export default App;