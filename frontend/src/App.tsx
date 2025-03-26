import { useState } from "react";
import FlightList from "./components/FlightList";
import SeatMap from "./components/SeatMap";

export default function App() {
    const [selectedFlight, setSelectedFlight] = useState<number | null>(null);

    return (
        <div className="container">
            <div className="left-panel">
                <FlightList onSelect={setSelectedFlight} />
            </div>
            <div className="right-panel">
                {selectedFlight && <SeatMap flightId={selectedFlight} />}
            </div>
        </div>
    );
}