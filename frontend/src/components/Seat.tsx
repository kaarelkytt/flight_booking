import "../styles/Seat.css";
import { Seat as SeatType } from "../types";

type Props = {
    seat: SeatType;
    selectedSeats: SeatType[];
    setSelectedSeats: (seats: SeatType[]) => void;
};

export default function Seat({ seat, selectedSeats, setSelectedSeats }: Props) {
    if (!seat.seatNumber) return <div className="no-seat"></div>;

    const isSelected = selectedSeats.includes(seat);
    const toggleSelect = () => {
        if (!seat.seatNumber || seat.occupied) return;
        setSelectedSeats(isSelected
            ? selectedSeats.filter(s => s.seatNumber !== seat.seatNumber)
            : [...selectedSeats, seat]);
    };

    return (
        <div
            className={`seat ${seat.occupied ? "occupied" : ""} ${seat.extraLegroom ? "extra-legroom" : ""} ${isSelected ? "selected" : ""}`}
            onClick={toggleSelect}
        >
            {seat.seatNumber}
        </div>
    );
}