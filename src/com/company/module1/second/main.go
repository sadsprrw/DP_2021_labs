package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

//Variant 6
//Task №8

var (
	random			 	= rand.New(rand.NewSource(time.Now().UnixNano()))
	maxCashBoxMoney 	= 1000
	partCashBoxMoney 	= 500
	maxStorageMoney     = 10000
	mutex 				= &sync.Mutex{}
	clientAccountId 	= -1
)

type Storage struct {
	money int
}

func newStorage() *Storage {
	return &Storage{
		money: random.Intn(maxStorageMoney) + 1000,
	}
}

type Cashbox struct {
	money int
}

func newCashbox() Cashbox {
	return Cashbox{
		money: random.Intn(maxCashBoxMoney) + 100,
	}
}

type ClientAccount struct {
	id 		int
	money 	int
}

func newClientAccount() ClientAccount {
	clientAccountId++

	return ClientAccount{
		id: clientAccountId,
		money: random.Intn(100),
	}
}

type Bank struct {
	storage 		*Storage
	cashBoxes 		[]Cashbox
	clientAccounts 	[]ClientAccount
}

func (b *Bank) observe() {
	for i := range b.cashBoxes {
		mutex.Lock()

		if b.storage.money < 0 {
			panic("Not enough money in the bank!")
		}

		if b.cashBoxes[i].money <= 0 {
			if b.storage.money >= maxStorageMoney {
				b.cashBoxes[i].money += maxCashBoxMoney
			}
		}

		if b.cashBoxes[i].money >= maxCashBoxMoney {
			b.storage.money += partCashBoxMoney
			b.cashBoxes[i].money -= partCashBoxMoney
		}

		mutex.Unlock()
	}
}

func newBank() *Bank {
	cashBoxes := make([]Cashbox, 10)
	clientAccounts := make([]ClientAccount, 20)

	for i := range cashBoxes {
		cashBoxes[i] = newCashbox()
	}

	for i := range clientAccounts {
		clientAccounts[i] = newClientAccount()
	}

	return &Bank{
		storage: newStorage(),
		cashBoxes: cashBoxes,
		clientAccounts: clientAccounts,
	}
}

func (b *Bank) getCashbox() *Cashbox {
	return &b.cashBoxes[random.Intn(len(b.cashBoxes))]
}

func (b *Bank) withdrawMoney(id, amount int) {
	mutex.Lock()
	if b.clientAccounts[id].money - amount >= 0 {
		b.clientAccounts[id].money -= amount
		b.getCashbox().money += amount
	}
	mutex.Unlock()
}

func (b *Bank) replenishMoney(id, amount int) {
	mutex.Lock()
	b.getCashbox().money -= amount
	b.clientAccounts[id].money += amount
	mutex.Unlock()
}

func (b *Bank) transferMoney(id, otherClientId, amount int) {
	mutex.Lock()
	if b.clientAccounts[id].money >= amount {
		b.clientAccounts[id].money -= amount
		b.clientAccounts[otherClientId].money += amount
	}
	mutex.Unlock()
}

func (b *Bank) pay(id, amount int) {
	mutex.Lock()
	if b.clientAccounts[id].money - amount >= 0 {
		b.clientAccounts[id].money -= amount
		b.getCashbox().money += amount
	}
	mutex.Unlock()
}

func (b *Bank) serveClients(clients []Client, endChan chan bool) {
	for i := range clients {
		clients[i].work(b)
	}
	rand.Shuffle(len(clients), func(i, j int) {
		clients[i], clients[j] = clients[j], clients[i]
	})
	endChan <- true
}

type Client struct {
	id int
}

func newClient(id int) Client {
	return Client{
		id: id,
	}
}

func (c *Client) withdrawMoney(b *Bank) {
	amount := random.Intn(10)
	fmt.Printf("Client %v withdraws %v\n", c.id, amount)
	b.withdrawMoney(c.id, amount)
}

func (c *Client) replenishMoney(b *Bank) {
	amount := random.Intn(10)
	fmt.Printf("Client %v supplements %v\n", c.id, amount)
	b.replenishMoney(c.id, amount)
}

func (c *Client) transferMoney(b *Bank, ) {
	amount := random.Intn(10)
	otherClientId := random.Intn(clientAccountId)
	fmt.Printf("Client %v transfer to %v amount %v\n", c.id, otherClientId, amount)
	b.transferMoney(c.id, otherClientId, amount)
}

func (c *Client) pay(b *Bank) {
	amount := random.Intn(10)
	fmt.Printf("Client %v paid %v\n", c.id, amount)
	b.pay(c.id, amount)
}

func (c *Client) work(b *Bank) {
	decision := rand.Intn(4)
	switch decision {
	case 0:
		c.withdrawMoney(b)
		break

	case 1:
		c.replenishMoney(b)
		break

	case 2:
		c.transferMoney(b)
		break

	case 3:
		c.pay(b)
		break
	}
	time.Sleep(time.Millisecond * time.Duration(500 + rand.Intn(700)))
}

func main() {
	bank := newBank()
	clients := make([]Client, clientAccountId + 1)
	fmt.Printf("Starting\n")
	endChan := make(chan bool, 1)
	for i := range clients {
		clients[i] = newClient(i)
	}
	go bank.observe()
	go bank.serveClients(clients, endChan)
	<-endChan
	fmt.Printf("Finished\n")
}
