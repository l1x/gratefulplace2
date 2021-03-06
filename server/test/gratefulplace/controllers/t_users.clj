(ns gratefulplace.controllers.t-users
  (:require [gratefulplace.db.test :as tdb]
            [gratefulplace.db.query :as q]
            [gratefulplace.db.manage :as db-manage]
            [gratefulplace.controllers.users :as users])
  (:use midje.sweet
        gratefulplace.paths
        gratefulplace.controllers.test-helpers))

(setup-db-background)

(fact "Users can change passwords"
  (fact "valid password change params update a user's password"
    (res :post
         (user-password-path (:id (auth)))
         {:current-password "password"
          :new-password "new-password"
          :new-password-confirmation "new-password"}
         (auth))
    => (contains {:status 201}))

  (fact "an unauthorized user can't change a password"
    (res :post
         (user-password-path (:id (auth)))
         {:current-password "password"
          :new-password "new-password"
          :new-password-confirmation "new-password"}
         (auth "joebob"))
    => (contains {:status 401})))


(facts "Users can update stuff"
  (fact "A user can update his own profile"
    (response-data :post (user-path (:id (auth))) {:about "new about" :email "daniel@flyingmachinestudios.com"} (auth))
    => (contains {"about" "new about"}))

  (fact "Your email address must look kind of like an email address"
    (res :post (user-path (:id (auth))) {:about "new about" :email "daniel"} (auth))
    => (contains {:status 400})))