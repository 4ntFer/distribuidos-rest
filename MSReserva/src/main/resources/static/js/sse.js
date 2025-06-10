const username = localStorage.getItem("username")

if(username){
    const eventSource = new EventSource(`http://localhost:8080/sse/?username=${username}`)

    eventSource.onmessage = function(event) {
        console.log("Notificação: " + event.data)
    }

    eventSource.addEventListener("pagamento-aprovado", (event) => {
        alert(`Pagamento da transação ${event.data} foi aprovado!`)
    })

    eventSource.addEventListener("pagamento-recusado", (event) => {
            alert(`Pagamento da transação ${event.data} foi recusado!`)
    })

    eventSource.addEventListener("promocao", (event) => {
                alert(`Oferta Recebida: ${event.data}`)
        })
}