import { useEffect, useState } from "react";
import { fetchSeatMap } from "../api";
import { SeatPlan } from "../types";

type Props = {
    flightId: number;
};

export default function SeatMap({ flightId }: Props) {
    const [seatPlan, setSeatPlan] = useState<SeatPlan | null>(null);

    useEffect(() => {
        fetchSeatMap(flightId)
            .then(setSeatPlan)
            .catch(err => console.error("Error fetching seat map:", err));
    }, [flightId]);

    if (!seatPlan) {
        return <p>Loading seat map...</p>;
    }

    return (
        <div>
            <h2>Seatmap</h2>
            <div className="seat-map">
                {seatPlan.seatRows.map((row, rowIndex) => (
                    <div key={rowIndex} className="seat-row">
                        {row.seats.map((seat, seatIndex) => (
                            <div
                                key={seatIndex}
                                className={`seat ${seat.occupied ? "occupied" : ""} ${seat.extraLegroom ? "extra-legroom" : ""}`}
                            >
                                {seat.seatNumber || "_"}
                            </div>
                        ))}
                    </div>
                ))}
            </div>
        </div>
    );
}