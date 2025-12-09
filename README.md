# **Bayan Uprising: Juan Revolution**

### *a game by Liwanag Studios*

### **Team Members**

* Capuyan, Alquien E.
* Carillo, Hazel Ann M.
* Saura, Jhon Lexter L.

---

<p align="center">
  <img src="/Card_Resources/BayanUprisingLogo-1.png" alt="Game Banner (Add your image here)" width="300" />
</p>

## 📘 **Overview**

**Bayan Uprising: Juan Revolution** is a 2D hybrid **platformer + turn-based card-battle game** inspired by real-world corruption issues in the Philippines. Players take the role of **Juan**, a Filipino citizen who rises against corrupt leaders—each transformed into symbolic creatures.

The game demonstrates practical application of **Data Structures, Algorithms, and Object-Oriented Programming (OOP)** concepts while delivering a socially relevant and engaging gameplay experience.

---

## 🎮 **Story Summary**

Liwanag Nation has fallen into corruption. Leaders who once promised hope now steal from the people, transforming into monstrous versions of their greed:

* 🐭 **Mang Gagantcho** – The Sneaky Barangay Captain
* 🐷 **Mayor Baboy Santos** – The Greedy City Mayor
* 🐀 **Gov. Squeaky Mapagmahal** – The Two-Faced Governor
* 🐗 **Director Baboyrambo** – The Abusive Regional Director
* 🐊 **Kroko Dalisay III** – The Corrupt President

Juan must journey through **five regions**, confronting each corrupt official through platforming challenges and strategic card battles.

---

## 🗺️ **Gameplay Flow**

### **1. Game Start**

A landing page with **Start** and **Exit** options introduces the player to the game.

### **2. Liwanag Nation – Game Map**

The central hub showing the **five regions**:

1. **Barangay Pinaasa**
2. **Sampaguita City**
3. **North Masigasig**
4. **Region 7-Eleven**
5. **Liwanag Palace**

Locked levels appear grayed out, while completed ones are marked with a ✔️.

Side buttons include:

* 📜 **Story** – Game lore, enemy info, card details
* 🧍 **Characters** – Shows available characters (only Juan is playable)
* 🃏 **Cards** – Complete list of player & enemy cards
* ❌ **Quit** – Exit confirmation popup

### **3. Platformer Stage**

Each level begins with a platformer environment featuring:

* Walking & jumping
* Gravity & falling death
* Collision detection
* Portal to enter card-battle mode

### **4. Card Battle Stage**

A turn-based battle system with:

* Attack, Heal, and Shield cards
* **3-card turn system** for both player and enemy
* **Mana/Moral Energy** cost system
* **Enemy AI** using random card selection

Win → unlock next level
Lose → retry or return to map

### **5. Game Completion**

A final popup appears after Level 5, congratulating the player and allowing a restart from Level 1.

---

## 🧩 **Playable Character**

### **Juan — The Main Protagonist**

| Level | HP  | Cost |
| ----- | --- | ---- |
| 1     | 100 | 4    |
| 2     | 120 | 6    |
| 3     | 150 | 8    |
| 4     | 180 | 9    |
| 5     | 200 | 10   |

**Description:**
A battle-hardened citizen whose strength grows each level. His stats scale to match increasingly difficult bosses.

---

## 🐉 **Enemies (Bosses)**

Each boss represents a form of corruption.

| Boss                       | Region   | HP   | Description                                             |
| -------------------------- | -------- | ---- | ------------------------------------------------------- |
| 🐭 Mang Gagantcho          | Barangay | 900  | Steals small amounts until nothing is left.             |
| 🐷 Mayor Baboy Santos      | City     | 1200 | Overprices projects; grows rich while the city suffers. |
| 🐀 Gov. Squeaky Mapagmahal | Province | 1500 | Acts like a hero but secretly steals public funds.      |
| 🐗 Director Baboyrambo     | Regional | 1700 | Friendly outside, corrupt inside; abuses workers.       |
| 🐊 Kroko Dalisay III       | National | 2000 | The final, most powerful corrupt leader.                |

---

## 🃏 **Cards Overview**

### **Player Cards**

* **Attack Cards** – Frost, Lightning, Sparks, Thunder, Holy Energy
* **Heal Card** – Restores HP
* **Shield Card** – Blocks incoming damage
* **Special Card** – Final powerful finishing move

### **Enemy Cards**

* **Attack Cards** – Ghost Summon, Lightning Strike
* **Heal Card** – Restores HP
* **Shield Card** – Dark barrier

---

## 🧠 **OOP Principles Used**

### **Platformer Mode**

| Principle     | Application                   | Benefit                           |
| ------------- | ----------------------------- | --------------------------------- |
| Encapsulation | Player, Portal, Spike classes | Protects state, easier debugging  |
| Inheritance   | GameObject parent class       | Reduces code duplication          |
| Polymorphism  | update(), draw() overrides    | Flexible object handling          |
| Abstraction   | State interfaces              | Clean separation of functionality |

### **Card Battle System**

| Principle     | Application                   | Benefit                       |
| ------------- | ----------------------------- | ----------------------------- |
| Encapsulation | Private Player & Enemy fields | Safe and organized data       |
| Inheritance   | GamePanel extends JPanel      | Code reuse                    |
| Polymorphism  | Runnable interface            | Smooth game loop              |
| Abstraction   | CardManager, DialogueManager  | Simplifies complex operations |

### **Game Map / Main Frame**

| Principle     | Application                 |
| ------------- | --------------------------- |
| Encapsulation | Level logic, UI states      |
| Inheritance   | JWindow/JPanel components   |
| Polymorphism  | Paint/render overrides      |
| Abstraction   | Sprite handling, navigation |

---

## 📚 **Data Structures Used**

### **Platformer Mode**

| Structure       | Usage                     |
| --------------- | ------------------------- |
| ArrayList       | Portals, spikes, entities |
| 2D int[][]      | Tile map for collisions   |
| BufferedImage[] | Animation frames          |
| Rectangle2D     | Hitboxes                  |
| Point           | Player spawn point        |

### **Card Battle System**

| Structure  | Usage                 |
| ---------- | --------------------- |
| ArrayList  | Discard pile          |
| Stack      | Draw pile             |
| Array (1D) | Enemy planned actions |
| Queue      | Dialogue lines        |

### **Game Map / Main Frame**

| Structure         | Usage                  |
| ----------------- | ---------------------- |
| ArrayList         | Animations, sprites    |
| Doubly LinkedList | Story image navigation |

---

## ✨ **Key Features**

* 🎴 Hybrid platformer + card battle system
* 🐉 Five progressive boss levels
* 📜 Story-driven gameplay based on corruption themes
* 🎨 Pixel art sprites and animations
* 🔊 Interactive UI with sound effects and music
* 🤖 Simple enemy AI for card selection
* ↻ Retry, continue, and restart systems

---

## 🛠️ **Tools & Technologies Used**

* **Language:** Java
* **Framework:** Java Swing
* **IDE:** Eclipse
* **Art Tools:** Photoshop, itch.io assets
* **References:** Tutorials (Kaarin Gaming, RyiSnow), AI-assisted debugging

---

## 🧪 **Challenges & Solutions**

* Difficulty synchronizing individually developed modules → **integrated step-by-step**
* Time limitation → reduced platformer complexity
* Limited hardware → optimized asset usage
* Complex OOP & DSA application → simplified but functional implementations

---

## 🏁 **Conclusion**

**Bayan Uprising: Juan Revolution** successfully applies DSA and OOP concepts inside a real game environment. Despite time, hardware, and learning challenges, the team delivered a functional and engaging game with a unique socio-political theme.

The project strengthened the team’s skills in programming, teamwork, planning, debugging, and user interface design.

---

## 📜 **License**

This project is licensed under the **MIT License**.

---

## ⭐ **Support the Project**

If you enjoyed the game, please give the repository a **⭐ star** and share your feedback!

---

> *“Liwanag ang pag-asa. Liwanag ang rebolusyon.”*
