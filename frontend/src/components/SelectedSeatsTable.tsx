import "../styles/SelectedSeatsTable.css";
import {Flight, Seat} from "../types.ts";

type Props = {
    selectedSeats: Seat[];
    setSelectedSeats: (seats: Seat[]) => void;
    flight: Flight;
};

export default function SelectedSeatsTable({selectedSeats, setSelectedSeats, flight}: Props) {
    const calculatePrice = (seat: Seat) => {
        let price = flight.initialPrice;
        if (seat.extraLegroom) price *= flight.extraLegroomMultiplier;
        if (seat.nearExit) price *= flight.nearExitMultiplier;
        return price.toFixed(2);
    };

    return (
        <div className="selected-seats">
            <h3>Selected Seats</h3>
            <div className="selected-seats-table-container">
                <table className="selected-seats-table">
                    <thead>
                    <tr>
                        <th>Seat</th>
                        <th>Extra legroom</th>
                        <th>Near exit</th>
                        <th>Price (€)</th>
                        <th>Remove</th>
                    </tr>
                    </thead>
                    <tbody>
                    {selectedSeats.map(seat => (
                        <tr key={seat.id}>
                            <td>{seat.seatNumber}</td>
                            <td>{seat.extraLegroom ? "✓" : ""}</td>
                            <td>{seat.nearExit ? "✓" : ""}</td>
                            <td>{calculatePrice(seat)}</td>
                            <td>
                                <button onClick={() => setSelectedSeats(selectedSeats.filter(s => s !== seat))}>X
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}