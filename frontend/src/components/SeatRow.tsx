import "../styles/SeatRow.css";
import Seat from "./Seat";
import { SeatRow as SeatRowType, Seat as SeatType } from "../types";

type Props = {
    row: SeatRowType;
    selectedSeats: SeatType[];
    setSelectedSeats: (seats: SeatType[]) => void;
};

export default function SeatRow({ row, selectedSeats, setSelectedSeats }: Props) {
    return (
        <div className="seat-row">
            {row.seats.map((seat, index) => (
                <Seat
                    key={index}
                    seat={seat}
                    selectedSeats={selectedSeats}
                    setSelectedSeats={setSelectedSeats}
                />
            ))}
        </div>
    );
}