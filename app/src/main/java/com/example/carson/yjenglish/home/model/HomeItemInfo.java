package com.example.carson.yjenglish.home.model;

import java.util.List;

/**
 * Created by 84594 on 2018/9/20.
 */

public class HomeItemInfo {
    private String status;
    private String msg;
    private HomeItemData data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public HomeItemData getData() {
        return data;
    }

    public void setData(HomeItemData data) {
        this.data = data;
    }

    public class HomeItemData {
        private String hot_comment_number;//热门评论数
        private String is_favour;//用户是否已经点了喜欢，0代表否，1代表是
        private String user_id;//作者id
        private List<Comment> new_comment;
        private String author_portrait;
        private String video;
        private String pic;
        private String type;
        private String title;
        private List<Recommendation> recommendations;
        private String is_like;//该feeds流文章用户是否点赞
        private List<Comment> hot_comment;
        private String id;
        private String author_username;
        private String new_comment_number;//最新评论数
        private String likes;//点赞数
        private String favours;//喜欢数
        private List<Order> order;

        public String getHot_comment_number() {
            return hot_comment_number;
        }

        public void setHot_comment_number(String hot_comment_number) {
            this.hot_comment_number = hot_comment_number;
        }

        public String getIs_favour() {
            return is_favour;
        }

        public void setIs_favour(String is_favour) {
            this.is_favour = is_favour;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public List<Comment> getNew_comment() {
            return new_comment;
        }

        public void setNew_comment(List<Comment> new_comment) {
            this.new_comment = new_comment;
        }

        public String getAuthor_portrait() {
            return author_portrait;
        }

        public void setAuthor_portrait(String author_portrait) {
            this.author_portrait = author_portrait;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Recommendation> getRecommendations() {
            return recommendations;
        }

        public void setRecommendations(List<Recommendation> recommendations) {
            this.recommendations = recommendations;
        }

        public String getIs_like() {
            return is_like;
        }

        public void setIs_like(String is_like) {
            this.is_like = is_like;
        }

        public List<Comment> getHot_comment() {
            return hot_comment;
        }

        public void setHot_comment(List<Comment> hot_comment) {
            this.hot_comment = hot_comment;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAuthor_username() {
            return author_username;
        }

        public void setAuthor_username(String author_username) {
            this.author_username = author_username;
        }

        public String getNew_comment_number() {
            return new_comment_number;
        }

        public void setNew_comment_number(String new_comment_number) {
            this.new_comment_number = new_comment_number;
        }

        public String getLikes() {
            return likes;
        }

        public void setLikes(String likes) {
            this.likes = likes;
        }

        public String getFavours() {
            return favours;
        }

        public void setFavours(String favours) {
            this.favours = favours;
        }

        public List<Order> getOrder() {
            return order;
        }

        public void setOrder(List<Order> order) {
            this.order = order;
        }

        public class Order {
            private String paragraph;
            private String pic;
            private String type;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getParagraph() {
                return paragraph;
            }

            public void setParagraph(String paragraph) {
                this.paragraph = paragraph;
            }
        }

        public class Comment {
            private String user_id;
            private String comments;//最新评论的被评论数
            private String set_time;//最新评论的发布时间
            private String is_like;//最新评论是否被点赞 1是 0否
            private List<InnerComment> inner_comment;
            private String comment;//最新评论的评论内容
            private String id;//最新评论的评论id
            private String portrait;
            private String likes;//最新评论的被点赞数
            private String username;

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getComments() {
                return comments;
            }

            public void setComments(String comments) {
                this.comments = comments;
            }

            public String getSet_time() {
                return set_time;
            }

            public void setSet_time(String set_time) {
                this.set_time = set_time;
            }

            public String getIs_like() {
                return is_like;
            }

            public void setIs_like(String is_like) {
                this.is_like = is_like;
            }

            public List<InnerComment> getInner_comment() {
                return inner_comment;
            }

            public void setInner_comment(List<InnerComment> inner_comment) {
                this.inner_comment = inner_comment;
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPortrait() {
                return portrait;
            }

            public void setPortrait(String portrait) {
                this.portrait = portrait;
            }

            public String getLikes() {
                return likes;
            }

            public void setLikes(String likes) {
                this.likes = likes;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public class InnerComment {
                private String id;//副评论id
                private String user_id;
                private String set_time;
                private String comment;
                private String portrait;
                private String username;
                private String likes;
                private String is_like;

                public String getUser_id() {
                    return user_id;
                }

                public void setUser_id(String user_id) {
                    this.user_id = user_id;
                }

                public String getSet_time() {
                    return set_time;
                }

                public void setSet_time(String set_time) {
                    this.set_time = set_time;
                }

                public String getComment() {
                    return comment;
                }

                public void setComment(String comment) {
                    this.comment = comment;
                }

                public String getPortrait() {
                    return portrait;
                }

                public void setPortrait(String portrait) {
                    this.portrait = portrait;
                }

                public String getUsername() {
                    return username;
                }

                public void setUsername(String username) {
                    this.username = username;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getLikes() {
                    return likes;
                }

                public void setLikes(String likes) {
                    this.likes = likes;
                }

                public String getIs_like() {
                    return is_like;
                }

                public void setIs_like(String is_like) {
                    this.is_like = is_like;
                }
            }
        }

        public class Recommendation {
            private String comments;
            private String kind;
            private String author_portrait;
            private String pic;
            private String video;
            private String id;
            private String type;
            private String title;
            private String author_username;
            private String likes;

            public String getComments() {
                return comments;
            }

            public void setComments(String comments) {
                this.comments = comments;
            }

            public String getKind() {
                return kind;
            }

            public void setKind(String kind) {
                this.kind = kind;
            }

            public String getAuthor_portrait() {
                return author_portrait;
            }

            public void setAuthor_portrait(String author_portrait) {
                this.author_portrait = author_portrait;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getVideo() {
                return video;
            }

            public void setVideo(String video) {
                this.video = video;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getAuthor_username() {
                return author_username;
            }

            public void setAuthor_username(String author_username) {
                this.author_username = author_username;
            }

            public String getLikes() {
                return likes;
            }

            public void setLikes(String likes) {
                this.likes = likes;
            }
        }
    }
}
