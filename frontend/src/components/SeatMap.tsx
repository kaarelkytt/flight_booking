import "../styles/SeatMap.css";
import {useEffect, useState} from "react";
import {fetchRecommendedSeats, fetchSeatMap} from "../api";
import {Flight, Seat, SeatPlan} from "../types";
import SeatRow from "./SeatRow";
import SeatSelection from "./SeatSelection";
import SelectedSeatsTable from "./SelectedSeatsTable.tsx";

type Props = {
    flight: Flight;
};

export default function SeatMap({flight}: Props) {
    const [seatPlan, setSeatPlan] = useState<SeatPlan | null>(null);
    const [selectedSeats, setSelectedSeats] = useState<Seat[]>([]);
    const [preferences, setPreferences] = useState({
        numSeats: 1,
        window: false,
        aisle: false,
        extraLegroom: false,
        nearExit: false,
        adjacent: false
    });

    useEffect(() => {
        fetchSeatMap(flight.id)
            .then(setSeatPlan)
            .catch(err => console.error("Error fetching seat map:", err));
    }, [flight.id]);

    const handleRecommendSeats = () => {
        fetchRecommendedSeats(flight.id, preferences, selectedSeats)
            .then(recommended => setSelectedSeats(recommended))
            .catch(err => console.error("Error fetching recommended seats:", err));
    };

    if (!seatPlan) {
        return <p>Loading seat map...</p>;
    }

    return (
        <div className="seatmap-container">
            <div className="seat-selection-container">
                <SeatSelection
                    preferences={preferences}
                    setPreferences={setPreferences}
                    onRecommendSeats={handleRecommendSeats}
                />
                <SelectedSeatsTable selectedSeats={selectedSeats}  setSelectedSeats={setSelectedSeats} flight={flight}/>
            </div>

            <div className="seat-map-container">
                <div className="seat-map">
                    {seatPlan.seatRows.map((row, index) => (
                        <SeatRow
                            key={index}
                            row={row}
                            selectedSeats={selectedSeats}
                            setSelectedSeats={setSelectedSeats}
                        />
                    ))}
                </div>
            </div>

        </div>
    );
}