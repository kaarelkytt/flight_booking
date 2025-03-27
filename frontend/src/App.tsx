import { useState } from "react";
import FlightList from "./components/FlightList";
import SeatMap from "./components/SeatMap";
import {Flight} from "./types.ts";

export default function App() {
    const [selectedFlight, setSelectedFlight] = useState<Flight | null>(null);

    return (
        <div className="container">
            <div className="left-panel">
                <FlightList onSelect={setSelectedFlight} />
            </div>
            <div className="right-panel">
                {selectedFlight && <SeatMap flight={selectedFlight} />}
            </div>
        </div>
    );
}