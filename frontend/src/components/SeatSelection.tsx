import "../styles/SeatSelection.css";

type Props = {
    preferences: any;
    setPreferences: (prefs: any) => void;
    onRecommendSeats: () => void;
};

export default function SeatSelection({ preferences, setPreferences, onRecommendSeats }: Props) {
    return (
        <div className="seat-selection">
            <h3>Choose Preferences</h3>
            <label>
                Number of seats:
                <input
                    type="number"
                    value={preferences.numSeats}
                    onChange={(e) => setPreferences({...preferences, numSeats: Number(e.target.value)})}
                    min="1" max="10"
                />
            </label>
            <label><input type="checkbox" checked={preferences.window} onChange={() => setPreferences({...preferences, window: !preferences.window})} /> Window seat</label>
            <label><input type="checkbox" checked={preferences.aisle} onChange={() => setPreferences({...preferences, aisle: !preferences.aisle})} /> Aisle seat</label>
            <label><input type="checkbox" checked={preferences.extraLegroom} onChange={() => setPreferences({...preferences, extraLegroom: !preferences.extraLegroom})} /> Extra legroom</label>
            <label><input type="checkbox" checked={preferences.nearExit} onChange={() => setPreferences({...preferences, nearExit: !preferences.nearExit})} /> Near exit</label>
            <label><input type="checkbox" checked={preferences.adjacent} onChange={() => setPreferences({...preferences, adjacent: !preferences.adjacent})} /> Adjacent seats</label>
            <button onClick={onRecommendSeats}>Recommend Seats</button>
        </div>
    );
}