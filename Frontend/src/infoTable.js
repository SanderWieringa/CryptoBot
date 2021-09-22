export const InfoTable = (props) => {
    const [data, setData] = useState({info: []})

    useEffect(() => {
        fetch("http://localhost:8080/info")
        .then((response) => response.json())
        .then((json) => setData(json))
        .catch(function(error) {
            console.log(error)
        });
    }, [])

    return (
        <div></div>
    )
}