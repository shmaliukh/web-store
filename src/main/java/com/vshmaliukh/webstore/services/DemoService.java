package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.login.LogInProvider;
import com.vshmaliukh.webstore.model.Category;
import com.vshmaliukh.webstore.model.User;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Comics;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Magazine;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Newspaper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class DemoService {

    // TODO implement test

    private final UserService userService;
    private final ItemService itemService;
    private final CategoryService categoryService;

    public void addDataToDatabase() {
        addItems();
        addUsers();
        addCategories();
    }

    private void addCategories() {
        Category category1 = new Category("Mystery", "Mystery novels, also called detective fiction, follow a detective solving a case from start to finish. They drop clues and slowly reveal information, turning the reader into a detective trying to solve the case, too. Mystery novels start with an exciting hook, keep readers interested with suspenseful pacing, and end with a satisfying conclusion that answers all of the reader’s outstanding questions.");
        Category category2 = new Category("Thriller", "Thriller novels are dark, mysterious, and suspenseful plot-driven stories. They very seldom include comedic elements, but what they lack in humor, they make up for in suspense. Thrillers keep readers on their toes and use plot twists, red herrings, and cliffhangers to keep them guessing until the end. Learn how to write your own thriller in Dan Brown’s MasterClass.");
        Category category3 = new Category("Horror", "Horror novels are meant to scare, startle, shock, and even repulse readers. Generally focusing on themes of death, demons, evil spirits, and the afterlife, they prey on fears with scary beings like ghosts, vampires, werewolves, witches, and monsters. In horror fiction, plot and characters are tools used to elicit a terrifying sense of dread. R.L. Stine’s MasterClass teaches tips and tricks for horror writing.");
        categoryService.save(category1);
        categoryService.save(category2);
        categoryService.save(category3);
    }

    private void addUsers() {
        User user1 = new User(null, "default username", "default@mail.com", LogInProvider.LOCAL, "admin", "admin", false);
        User user2 = new User(null, "Vlad", "zskat@gmail.com", LogInProvider.LOCAL, "admin", "admin", true);
        userService.save(user1);
        userService.save(user2);
    }

    private void addItems() {
        addBooks();
        addNewspapers();
        addMagazines();
        addComics();
    }

    private void addBooks() {
        Book book1 = new Book(null, "The Creative Act", 10, 3, 25, 31,
                "Many famed music producers are known for a particular sound that has its day and then ages out. Rick Rubin is known for something else: creating a space where artists of all different genres and traditions can home in on who they really are and what they really offer. He has made a practice of helping people transcend their self-imposed expectations in order to reconnect with a state of innocence from which the surprising becomes inevitable.\n" +
                        "Over the years, as he has thought deeply about where creativity comes from and where it doesn't, he has learned that being an artist isn't about your specific output; it's about your relationship to the world. Creativity has a place in everyone's life, and everyone can make that place larger. In fact, there are few more important responsibilities.\n" +
                        "The Creative Act is a beautiful and generous course of study that illuminates the path of the artist as a road we all can follow. It distils the wisdom gleaned from a lifetime's work into a luminous reading experience that puts the power to create moments - and lifetimes - of exhilaration and transcendence within closer reach for all of us.",
                "In stock", true, 7, 540, "Rick Rubin", new Date());
        Book book2 = new Book(null, "The Shards", 7, 1, 15, 27,
                "Los Angeles, 1981 ?17-year-old Bret is a senior at the exclusive Buckley prep school when a new student arrives with a mysterious past. Robert Mallory is bright, handsome, charismatic, and shielding a secret from Bret and his friends, even as he becomes a part of their tightly knit circle. Bret's obsession with Mallory is equalled only by his increasingly unsettling preoccupation with The Trawler, a serial killer on the loose who seems to be drawing ever closer to Bret and his friends, taunting them with grotesque threats and horrific, sharply local acts of violence.\n" +
                        "Can he trust his friends ? or his own mind ? to make sense of the danger they appear to be in? Thwarted by the world and by his own innate desires, buffeted by unhealthy fixations, Bret spirals into paranoia and isolation as the relationship between The Trawler and Robert Mallory hurtles inexorably toward a collision.\n" +
                        "Gripping, sly, suspenseful, deeply haunting and often darkly funny, The Shards is a mesmerizing fusing of fact and fiction that brilliantly explores the emotional fabric of Bret's life at 17 ? sex and jealousy, obsession and murderous rage.",
                "In stock", true, 6, 608, "Bret Easton Ellis", new Date());
        Book book3 = new Book(null, "Me vs Brain : An Overthinker's Guide to Life", 2, 0, 21, 23,
                "Brain: We left the oven on!\n" +
                        "Me: No don't say that, I've not got time for this!\n" +
                        "Brain: The house is probably on fire!\n" +
                        "Me: Stop it, I need to write this book description.\n" +
                        "Brain: But the blazing fire.\n" +
                        "Me: We didn't even use the oven today.\n" +
                        "Brain: But what if -\n" +
                        "Me: No. We're not doing this, I'm finishing this description.\n" +
                        "Hello there! Hayley Morris here. Or you might know me as the Brain Girl, and don't be fooled...it's not because I'm outrageously smart. Just an avid overthinker. I'm on a mission to prove once and for all that You. Are. Normal. Running through imaginary arguments whilst showering, hiding your knickers in the nurse's office before they look directly into your vagina, or not knowing how to be a normal human when you have the plumber over. I've spent the majority of my life saying and doing embarrassing things that wake me up in a cold sweat at 3am as my Brain reminds me of every minor detail.\n" +
                        "In this book, I've overthought absolutely everything so you don't have to. I'll be talking about everything from dating to discharge, mental health to menstrual cups. I might not be able to banish your anxiety or make you feel 100% comfortable in your skin, but I hope I can at least give you a break from the constant brain chatter and we can rejoice and laugh at how similar we actually all are.",
                "Preorder", false, 0, 304, "Hayley Morris", new Date());
        Book book4 = new Book(null, "default book name", 100, 100, 10, 10,
                "default book description",
                "Backorder", false, 10, 10, "default book author", new Date());
        itemService.saveItem(book1);
        itemService.saveItem(book2);
        itemService.saveItem(book3);
        itemService.saveItem(book4);
    }

    private void addNewspapers() {
        Newspaper newspaper1 = new Newspaper(null, "The Guardian", 100, 100, 2, 3,
                "Windrush campaigners have described the government's decision to drop crucial reform commitments made in the wake of the Home Office scandal as a \"slap in the face\", The Guardian reports.",
                "In stock", true, 13211, 10);
        Newspaper newspaper2 = new Newspaper(null, "Daily Express", 65, 32, 3, 4,
                "Senior Tories have called on the chancellor to ease the cost of living crisis by \"going for growth\", despite his warnings of a tough year ahead, the Daily Express reports.",
                "In stock", true, 568, 14);
        Newspaper newspaper3 = new Newspaper(null, "The Daily Telegraph", 22, 1, 1, 1,
                "Scottish First Minister Nicola Sturgeon's \"controversial trans laws were last night in disarray\" after she was forced to announce a rapist who \"claimed to be female would be moved into a male prison\", The Daily Telegraph writes.",
                "In stock", true, 21, 6);
        Newspaper newspaper4 = new Newspaper(null, "default newspaper name", 100, 100, 10, 10,
                "default newspaper description",
                "Backorder", false, 10, 10);
        itemService.saveItem(newspaper1);
        itemService.saveItem(newspaper2);
        itemService.saveItem(newspaper3);
        itemService.saveItem(newspaper4);
    }

    private void addMagazines() {
        Magazine magazine1 = new Magazine(null, "Amateur Gardening", 10, 7, 4, 5,
                "Our experts will help and inspire you to create your perfect garden, with great ideas for plant combinations, new plants try, top tips for growing veg and a unique reader advice service.",
                "In stock", true, 3, 66);
        Magazine magazine2 = new Magazine(null, "Homes & Gardens Magazine Subscription", 21, 21, 3, 4,
                "Homes & Gardens has been shaping British style for 100 years. Aspirational, stylish and sophisticated, it showcases the best in interiors, decorating, gardens and entertaining inspiration.\n" +
                        "Every month Homes & Gardens is packed full of beautiful houses in Britain and abroad, the latest decorating trends, the most stylish fabrics, furniture and accessories, expert advice and glorious gardens. Every interior style is covered, from classic to eclectic, plus you'll find stylish shopping and delicious recipes.",
                "In stock", true, 33, 56);
        Magazine magazine3 = new Magazine(null, "TV&Satellite Week Magazine Subscription", 10, 0, 3, 3,
                "TV&Satellite Week lists more channels than any other UK TV magazine, making it the must-have guide for any discerning viewer. With clear, easy-to-use listings plus comprehensive previews of the week's best new TV shows, films and sport, TV&Satellite Week is the must-have guide for any Virgin or Sky subscriber. TV&Satellite Week guides its readers to the most exciting and innovative programmes on digital TV.",
                "Backorder", true, 0, 32);
        Magazine magazine4 = new Magazine(null, "default magazine name", 100, 100, 10, 10,
                "default magazine description",
                "Backorder", false, 10, 10);
        itemService.saveItem(magazine1);
        itemService.saveItem(magazine2);
        itemService.saveItem(magazine3);
        itemService.saveItem(magazine4);
    }

    private void addComics() {
        Comics comics1 = new Comics(null, "Star Wars: The High Republic - The Blade", 0, 0, 3, 4,
                "Jedi Master Porter Engle has journeyed with his sister, Barash, to a planet far on the Republic frontier in response to a desperate request for aid. They are certain they will succeed. No one in the galaxy fights like Jedi Porter Engle. No warrior can stand against him. No one even comes close. They are certain...until they see what awaits them.",
                "Out of stock", false, 88, 20, "marvel");
        Comics comics2 = new Comics(null, "Star Wars: Yoda (2022) #3", 7, 0, 3, 4,
                "Yoda’s experiment with the Scalvi has ended in disaster, the situation on Turrak worse than ever. A new generation has risen up since the Jedi Master first came to the planet, a generation that still hasn’t learned the most important lesson of all. Will Yoda abandon those who need him most in their hour of need?",
                "Out of stock", true, 999, 24, "marvel");
        Comics comics3 = new Comics(null, "\n" +
                "INCREDIBLE HULK 347 FACSIMILE EDITION (2023) #1", 40, 0, 4, 5,
                "One of the most startling transformations in the ever-incredible history of the Hulk! The gray-skinned goliath has been missing for months, since he was caught in a massive gamma bomb blast detonated by the Leader. But in Las Vegas, casino owner Michael Berengetti has a new bodyguard who redefines the meaning of “muscle.” Suited and booted, the man called Mr. Fixit is a hulking brute with skin the color of granite — and, when one of Berengetti’s rivals makes a move against him, he’ll soon learn Fixit is bulletproof too! The Hulk finally has a job he enjoys, and he’s living the high life — so what could go wrong? A new era in Hulk history begins here! It’s one of the all-time great Marvel comic books, boldly re-presented in its original form, ads and all! Reprinting INCREDIBLE HULK (1968) #347.",
                "Backorder", true, 0, 20, "marvel");
        Comics comics4 = new Comics(null, "default comics name", 100, 100, 10, 10,
                "default comics description",
                "Backorder", false, 10, 10, "default comics publisher");
        itemService.saveItem(comics1);
        itemService.saveItem(comics2);
        itemService.saveItem(comics3);
        itemService.saveItem(comics4);
    }

}
